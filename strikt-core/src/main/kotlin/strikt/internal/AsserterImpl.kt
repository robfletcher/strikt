package strikt.internal

import strikt.api.Asserter
import strikt.api.AssertionComposer
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.CompoundAssertions
import strikt.api.Status
import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.Mode.COLLECT
import strikt.internal.Mode.FAIL_FAST

internal class AsserterImpl<T>(
  private val context: AssertionGroup<T>,
  private val mode: Mode, // TODO: can we replace with with just checking for context being top level?
  private val negated: Boolean = false
) : Asserter<T> {

  override fun describedAs(description: String): Asserter<T> {
    context.description = description
    return this
  }

  override fun assertAll(block: Asserter<T>.() -> Unit): Asserter<T> =
    AsserterImpl(context, COLLECT, negated)
      .apply(block)
      .also {
        throwOnFailure()
      }

  override fun assert(
    description: String,
    expected: Any?,
    assert: AtomicAssertion<T>.() -> Unit
  ): AsserterImpl<T> {
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

    val composer = object : AssertionComposer<T> {
      override fun describedAs(description: String): Asserter<T> {
        TODO("not implemented")
      }

      override fun assertAll(block: Asserter<T>.() -> Unit): Asserter<T> {
        TODO("not implemented")
      }

      override fun compose(description: String, expected: Any?, assertions: AssertionComposer<T>.() -> Unit): CompoundAssertions<T> {
        TODO("not implemented")
      }

      override fun <R> map(description: String, function: T.() -> R): Asserter<R> {
        TODO("not implemented")
      }

      override fun not(): Asserter<T> {
        TODO("not implemented")
      }

      override val subject: T
        get() = context.subject

      override fun <E> expect(subject: E): Asserter<E> =
        AsserterImpl(AssertionSubject(composedContext, subject), COLLECT)

      override fun <E> expect(subject: E, block: Asserter<E>.() -> Unit): Asserter<E> =
        expect(subject).assertAll(block)

      override fun assert(description: String, assert: AtomicAssertion<T>.() -> Unit): Asserter<T> =
        AsserterImpl(composedContext, COLLECT).assert(description, assert)

      override fun assert(description: String, expected: Any?, assert: AtomicAssertion<T>.() -> Unit): Asserter<T> =
        AsserterImpl(composedContext, COLLECT).assert(description, expected, assert)
    }
    composer.apply(assertions)
    return composedContext.let { result ->
      object : CompoundAssertions<T> {
        override fun then(block: CompoundAssertion<T>.() -> Unit): Asserter<T> {
          result.block()
          throwOnFailure()
          return this@AsserterImpl
        }
      }
    }
  }

  override fun <R> map(description: String, function: T.() -> R): Asserter<R> =
    context.subject.function()
      .let { mappedValue ->
        AsserterImpl(
          AssertionSubject<R>(context, mappedValue, description),
          mode,
          negated
        )
      }

  override fun not(): Asserter<T> = AsserterImpl(
    AssertionSubject(context, context.subject, "%s does not match"),
    mode,
    !negated
  )

  private fun throwOnFailure() {
    if (mode == FAIL_FAST) {
      context.throwOnFailure()
    }
  }
}
