package strikt.internal

import strikt.api.Asserter
import strikt.api.AssertionComposer
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.CompoundAssertions
import strikt.internal.Mode.COLLECT
import strikt.internal.Mode.FAIL_FAST

internal class AsserterImpl<T>(
  private val context: AssertionSubject<T>,
  private val mode: Mode,
  private val negated: Boolean = false
) : Asserter<T> {

  override fun describedAs(description: String): Asserter<T> =
    AsserterImpl(context, mode, negated)

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
    AtomicAssertionResult(context, description, expected).assert()
    throwOnFailure()
    return this
  }

  override fun compose(
    description: String,
    expected: Any?,
    assertions: AssertionComposer<T>.() -> Unit
  ): CompoundAssertions<T> {
    val composedContext = CompoundAssertionResult(context, description, expected)
    val composer = object : AssertionComposer<T> {
      override val subject: T
        get() = this@AsserterImpl.context.subject.value // TODO: c'mon, train wreck

      override fun <E> expect(subject: E): Asserter<E> =
        AsserterImpl(AssertionSubject(composedContext, subject), COLLECT)

      override fun <E> expect(subject: E, block: Asserter<E>.() -> Unit): Asserter<E> =
        expect(subject).assertAll(block)

      override fun assert(description: String, assertion: AtomicAssertion<T>.() -> Unit): Asserter<T> =
        AsserterImpl(this@AsserterImpl.context, COLLECT).assert(description, assertion)

      override fun assert(description: String, expected: Any?, assertion: AtomicAssertion<T>.() -> Unit): Asserter<T> =
        AsserterImpl(this@AsserterImpl.context, COLLECT).assert(description, expected, assertion)
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
    context.subject.value.function()
      .let { mappedValue ->
        AsserterImpl(
          AssertionSubject<R>(context, Described(mappedValue, description)),
          mode,
          negated
        )
      }

  /**
   * Reverses any assertions chained after this method.
   *
   * @sample strikt.samples.AssertionMethods.not
   *
   * @return an assertion that negates the results of any assertions applied to
   * its subject.
   */
  override fun not(): Asserter<T> = AsserterImpl(context, mode, !negated)

  private fun throwOnFailure() {
    if (mode == FAIL_FAST) {
      context.throwOnFailure()
    }
  }
}
