package strikt.internal

import org.opentest4j.AssertionFailedError
import strikt.api.Status
import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.opentest4j.AssertionFailed
import strikt.internal.opentest4j.CompoundAssertionFailure
import strikt.internal.opentest4j.IncompleteAssertion
import strikt.internal.reporting.writePartialToString
import strikt.internal.reporting.writeToString

internal sealed class AssertionStrategy {

  fun <T> appendAtomic(
    context: AssertionGroup<T>,
    description: String,
    expected: Any?,
  ): AtomicAssertionNode<T> =
    object : AtomicAssertionNode<T>(
      context,
      { provideDescription(description) },
      expected
    ) {

      override var status: Status = Pending
        private set

      override fun pass(description: String?) {
        status = onPass(description = description)
        afterStatusSet(this)
      }

      override fun pass(actual: Any?, description: String?) {
        status = onPass(
          description = description,
          comparison = ComparedValues(expected, actual)
        )
        afterStatusSet(this)
      }

      override fun fail(description: String?, cause: Throwable?) {
        status = onFail(description = description, cause = cause)
        afterStatusSet(this)
      }

      override fun fail(actual: Any?, description: String?, cause: Throwable?) {
        status = onFail(
          description = description,
          comparison = ComparedValues(expected, actual),
          cause = cause
        )
        afterStatusSet(this)
      }
    }

  fun <T> appendCompound(
    context: AssertionGroup<T>,
    description: String,
    expected: Any?,
  ): CompoundAssertionNode<T> =
    object : CompoundAssertionNode<T>(
      context,
      { provideDescription(description) },
      expected
    ) {

      override var status: Status = Pending
        private set

      override fun pass(description: String?) {
        status = onPass(description)
        afterStatusSet(this)
      }

      override fun fail(description: String?, cause: Throwable?) {
        status = onFail(description = description, cause = cause)
        afterStatusSet(this)
      }

      override val anyFailed: Boolean
        get() = children.any { it.status is Failed }
      override val allFailed: Boolean
        get() = children.all { it.status is Failed }
      override val anyPassed: Boolean
        get() = children.any { it.status is Passed }
      override val allPassed: Boolean
        get() = children.all { it.status is Passed }
      override val passedCount: Int
        get() = children.count { it.status is Passed }
      override val failedCount: Int
        get() = children.count { it.status is Failed }
    }

  open fun evaluate(tree: AssertionGroup<*>) {}

  open fun evaluate(trees: Collection<AssertionGroup<*>>) {}

  protected open fun provideDescription(default: String) = default

  protected open fun <T> afterStatusSet(result: AssertionResult<T>) {}

  protected open fun onPass(
    description: String? = null,
    comparison: ComparedValues? = null,
  ): Status = Passed(description, comparison)

  protected open fun onFail(
    description: String? = null,
    comparison: ComparedValues? = null,
    cause: Throwable? = null,
  ): Status = Failed(description, comparison, cause)

  object Collecting : AssertionStrategy()

  object Throwing : AssertionStrategy() {
    override fun evaluate(tree: AssertionGroup<*>) {
      if (tree.status is Failed) {
        throw CompoundAssertionFailure(
          tree.root.writeToString(),
          tree.findFailureNodes()
            .map {
              createAssertionFailedError(
                it.writePartialToString(),
                it.status as Failed
              )
            }
        )
      } else if (tree.status is Pending) {
        throw IncompleteAssertion()
      }
    }

    /**
     * Find the failing leaf nodes in an assertion result graph.
     */
    private fun AssertionGroup<*>.findFailureNodes(): List<AssertionNode<*>> {
      return children
        .filter { it.status is Failed }
        .map { node ->
          when (node) {
            is AssertionChain -> node.children.first { it.status is Failed }
            is AssertionChainedGroup -> node.children.first { it.status is Failed }
            else -> node
          }
        }
    }

    override fun evaluate(trees: Collection<AssertionGroup<*>>) {
      if (trees.any { it.status is Failed }) {
        val failures = trees
          .filter { it.status is Failed }
          .map {
            createAssertionFailedError(
              it.writeToString(),
              it.status as Failed
            )
          }
        throw CompoundAssertionFailure(trees.writeToString(), failures)
      }
    }

    override fun <T> afterStatusSet(result: AssertionResult<T>) {
      val status = result.status
      if (status is Failed) throw createAssertionFailedError(
        result.root.writeToString(),
        status
      )
    }
  }

  class Negating(
    private val delegate: AssertionStrategy,
  ) : AssertionStrategy() {
    override fun provideDescription(default: String) =
      listOf(
        Regex("^is not\\b") to "is",
        Regex("^is\\b") to "is not",
        Regex("^contains\\b") to "does not contain",
        Regex("^starts with\\b") to "does not start with",
        Regex("^ends with\\b") to "does not end with",
        Regex("^matches\\b") to "does not match",
        Regex("^throws\\b") to "does not throw",
        Regex("^has\\b") to "does not have"
      ).find { (regex, _) ->
        regex.containsMatchIn(default)
      }?.let { (regex, replacement) ->
        default.replace(regex, replacement)
      } ?: "does not match: $default"

    override fun onPass(
      description: String?,
      comparison: ComparedValues?,
    ): Status = Failed(description, comparison)

    override fun onFail(
      description: String?,
      comparison: ComparedValues?,
      cause: Throwable?,
    ) = Passed(description, comparison)

    override fun <T> afterStatusSet(result: AssertionResult<T>) {
      delegate.afterStatusSet(result)
    }
  }

  internal fun createAssertionFailedError(
    message: String,
    failed: Failed?,
  ): AssertionFailedError {
    val error = if (failed?.comparison != null)
      AssertionFailed(
        message,
        failed.comparison.expected,
        failed.comparison.actual,
        failed.cause
      )
    else
      AssertionFailed(
        message,
        failed?.cause
      )

    val stackTrace = error.stackTrace
    val lastIndex = stackTrace
      .indexOfLast { it.className.startsWith("strikt") }
    val suppressedElements = stackTrace.copyOfRange(0, lastIndex)
    val remainingElements = stackTrace.copyOfRange(lastIndex + 1, stackTrace.lastIndex)
    error.stackTrace = remainingElements
    val striktError = AssertionFailedError()
    striktError.stackTrace = suppressedElements
    error.addSuppressed(striktError)
    return error
  }
}
