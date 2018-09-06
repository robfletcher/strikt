package strikt.internal

import strikt.api.Assertion
import strikt.api.Assertion.Builder
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.CompoundAssertions
import strikt.api.DescribeableBuilder
import strikt.api.Status
import strikt.api.Status.AssertionFailed
import strikt.api.Status.ComparisonFailed
import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.Mode.COLLECT
import strikt.internal.Mode.FAIL_FAST

internal class AssertionBuilder<T>(
  private val context: AssertionGroup<T>,
  private val mode: Mode, // TODO: can we replace with with just checking for context being top level?
  private val negated: Boolean = false
) : DescribeableBuilder<T> {

  override fun describedAs(description: String): Builder<T> {
    context.description = description
    return this
  }

  override fun and(
    assertions: Assertion.Builder<T>.() -> Unit
  ): Assertion.Builder<T> {
    AssertionBuilder(context, COLLECT, negated)
      .also { nestedBuilder ->
        nestedBuilder.assertions()
        if (mode == FAIL_FAST) {
          context.toError()?.let { throw it }
        }
      }
    return this
  }

  override fun assert(
    description: String,
    expected: Any?,
    assert: AtomicAssertion.(T) -> Unit
  ): AssertionBuilder<T> {
    object : AtomicAssertionNode<T>(context, description, expected), AtomicAssertion {
      override val subject: T
        get() = context.subject

      private var _status: Status = Pending
      override val status: Status
        get() = _status

      override fun pass() {
        _status = when {
          negated -> AssertionFailed()
          else -> Passed
        }
      }

      override fun fail(description: String?, cause: Throwable?) {
        _status = when {
          negated -> Passed
          else -> AssertionFailed(description, cause)
        }
      }

      override fun fail(actual: Any?, description: String?, cause: Throwable?) {
        _status = when {
          negated -> Passed
          else -> ComparisonFailed(expected, actual, description, cause)
        }
      }
    }
      .assert(context.subject)
    throwOnFailure()
    return this
  }

  override fun compose(
    description: String,
    expected: Any?,
    assertions: Builder<T>.(T) -> Unit
  ): CompoundAssertions<T> {
    val composedContext = object : CompoundAssertionNode<T>(context, description, expected), CompoundAssertion {
      override val subject: T
        get() = context.subject

      private var _status: Status = Pending
      override val status: Status
        get() = _status

      override fun pass() {
        _status = when {
          negated -> AssertionFailed()
          else -> Passed
        }
      }

      override fun fail(description: String?, cause: Throwable?) {
        _status = when {
          negated -> Passed
          else -> AssertionFailed(description, cause)
        }
      }

      override val anyFailed: Boolean
        get() = children.any { it.status is Failed }
      override val allFailed: Boolean
        get() = children.all { it.status is Failed }
      override val anyPassed: Boolean
        get() = children.any { it.status is Passed } xor (negated)
      override val allPassed: Boolean
        get() = children.all { it.status is Passed } xor (negated)
    }

    AssertionBuilder(composedContext, COLLECT, negated).apply {
      assertions(context.subject)
    }
    return composedContext.let { result ->
      object : CompoundAssertions<T> {
        override fun then(block: CompoundAssertion.() -> Unit): Builder<T> {
          result.block()
          throwOnFailure()
          return this@AssertionBuilder
        }
      }
    }
  }

  override fun <R> map(description: String, function: (T) -> R): DescribeableBuilder<R> =
    function(context.subject)
      .let { mappedValue ->
        AssertionBuilder(
          AssertionSubject(context, mappedValue, description),
          mode,
          negated
        )
      }

  override fun not(): Builder<T> = AssertionBuilder(
    AssertionSubject(context, context.subject, "does not match"),
    mode,
    !negated
  )

  private fun throwOnFailure() {
    if (mode == FAIL_FAST) {
      context.toError()?.let { throw it }
    }
  }
}
