package strikt.internal

import strikt.api.Assertion.Builder
import strikt.api.AssertionComposer
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.CompoundAssertions
import strikt.api.DescribeableBuilder
import strikt.api.Status
import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.Mode.COLLECT
import strikt.internal.Mode.FAIL_FAST

internal class AssertionBuilder<T>(
  private val context: AssertionGroup<T>,
  private val mode: Mode, // TODO: can we replace with with just checking for context being top level?
  private val negated: Boolean = false
) : DescribeableBuilder<T>, AssertionComposer<T> {

  override fun describedAs(description: String): Builder<T> {
    context.description = description
    return this
  }

  override fun assert(
    description: String,
    expected: Any?,
    assert: AtomicAssertion<T>.() -> Unit
  ): AssertionBuilder<T> {
    object : AtomicAssertionNode<T>(context, description, expected), AtomicAssertion<T> {
      override val subject: T
        get() = context.subject

      private var _status: Status = Pending
      override val status: Status
        get() = _status

      override fun pass() {
        _status = if (negated) {
          Failed()
        } else {
          Passed
        }
      }

      override fun fail(actual: Any?, description: String?, cause: Throwable?) {
        _status = if (negated) {
          Passed
        } else {
          Failed(actual, description, cause)
        }
      }
    }
      .assert()
    throwOnFailure()
    return this
  }

  override fun compose(
    description: String,
    expected: Any?,
    assertions: AssertionComposer<T>.() -> Unit
  ): CompoundAssertions<T> {
    val composedContext = object : CompoundAssertionNode<T>(context, description, expected), CompoundAssertion<T> {
      override val subject: T
        get() = context.subject

      private var _status: Status = Pending
      override val status: Status
        get() = _status

      override fun pass() {
        _status = if (negated) {
          Failed()
        } else {
          Passed
        }
      }

      override fun fail(actual: Any?, description: String?, cause: Throwable?) {
        _status = if (negated) {
          Passed
        } else {
          Failed(actual, description, cause)
        }
      }

      override val anyFailed: Boolean
        get() = children.any { it.status is Failed }
      override val allFailed: Boolean
        get() = children.all { it.status is Failed }
      override val anyPassed: Boolean
        get() = children.any { it.status is Passed }
      override val allPassed: Boolean
        get() = children.all { it.status is Passed }
    }

    AssertionBuilder(composedContext, COLLECT, negated).apply(assertions)
    return composedContext.let { result ->
      object : CompoundAssertions<T> {
        override fun then(block: CompoundAssertion<T>.() -> Unit): Builder<T> {
          result.block()
          throwOnFailure()
          return this@AssertionBuilder
        }
      }
    }
  }

  override fun <R> map(description: String, function: T.() -> R): DescribeableBuilder<R> =
    context.subject.function()
      .let { mappedValue ->
        AssertionBuilder(
          AssertionSubject(context, mappedValue, description),
          mode,
          negated
        )
      }

  override fun not(): Builder<T> = AssertionBuilder(
    AssertionSubject(context, context.subject, "%s does not match"),
    mode,
    !negated
  )

  override val subject: T
    get() = context.subject

  private fun throwOnFailure() {
    if (mode == FAIL_FAST) {
      context.toError()?.let { throw it }
    }
  }
}
