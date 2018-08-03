package strikt.internal

import strikt.api.Asserter
import strikt.api.AssertionComposer
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.CompoundAssertions
import strikt.internal.Mode.COLLECT

internal class AsserterImpl<T>(
  private val context: AssertionSubject<T>,
  private val mode: Mode,
  private val negated: Boolean = false
) : Asserter<T> {

  override fun describedAs(description: String): Asserter<T> =
    AsserterImpl(context, mode, negated)

  override fun evaluate(block: Asserter<T>.() -> Unit): Asserter<T> =
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
    return this
  }

  override fun compose(
    description: String,
    expected: Any?,
    assertions: AssertionComposer<T>.() -> Unit
  ): CompoundAssertions<T> =
    CompoundAssertionResult(context, description, expected)
      .apply(assertions)
      .let { result ->
        object : CompoundAssertions<T> {
          override fun then(block: CompoundAssertion<T>.() -> Unit): Asserter<T> {
            result.block()
            return this@AsserterImpl
          }
        }
      }

  override fun <R> map(description: String, function: T.() -> R): Asserter<R> =
    context.subject.value.function()
      .let { mappedValue ->
        AsserterImpl(
          AssertionSubject(context, Described(mappedValue, description)),
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
    TODO("not implemented")
  }
}
