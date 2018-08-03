package strikt.internal

import strikt.api.Asserter
import strikt.api.AssertionComposer
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.CompoundAssertions
import strikt.api.Status
import strikt.internal.Mode.COLLECT

// TODO: this should be stateless and just responsible for appending to a tree of results
internal class AsserterImpl<T>(
  override val parent: ResultNode? = null,
  override val subject: Described<T>,
  private val mode: Mode,
  private val negated: Boolean = false
) : Asserter<T>, AssertionSubject<T> {

  override val status: Status
    get() = when {
      assertions.isEmpty() -> Status.Pending
      assertions.any { it.status is Status.Pending } -> Status.Pending
      assertions.any { it.status is Status.Failed } -> Status.Failed()
      else -> Status.Passed
    }

  override val children: List<ResultNode>
    get() = assertions

  private val assertions =
    mutableListOf<AssertionResult<*>>()

  private fun append(assertion: AssertionResult<*>) =
    assertions.add(assertion)

  override fun describedAs(description: String): Asserter<T> =
    apply {
      this.description = description
    }

  override fun evaluate(block: Asserter<T>.() -> Unit): Asserter<T> =
    AsserterImpl(parent, subject, COLLECT, negated)
      .apply(block)
      .also {
        throwOnFailure()
      }

  override fun assert(
    description: String,
    expected: Any?,
    assert: AtomicAssertion<T>.() -> Unit
  ) =
    apply {
      AtomicAssertionResult(subject.value, parent)
        .also { append(it) }
        .apply(assert)
    }

  override fun compose(
    description: String,
    expected: Any?,
    assertions: AssertionComposer<T>.() -> Unit
  ): CompoundAssertions<T> =
    CompoundAssertionResult(subject.value, parent)
      .also { append(it) }
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
    AsserterImpl(
      parent = this,
      subject = Described(subject.value.function(), description),
      mode = mode
    )

  /**
   * Reverses any assertions chained after this method.
   *
   * @sample strikt.samples.AssertionMethods.not
   *
   * @return an assertion that negates the results of any assertions applied to
   * its subject.
   */
  override fun not(): Asserter<T> = AsserterImpl(parent, subject, mode, !negated)
}
