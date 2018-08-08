package strikt.api

/**
 * Allows assertions to be composed, or nested.
 * This class is the receiver of the lambda passed to
 * [Asserter.compose].
 *
 * @property subject The subject of the assertion.
 */
interface AssertionComposer<T> : Asserter<T> { // TODO: this must extend Asserter<T> or both share a common interface with is where extensions hook
  val subject: T

  /**
   * Start a chain of assertions in the current nested context.
   *
   * @param subject the subject of the chain of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @return an assertion for [subject].
   */
  fun <E> expect(subject: E): Asserter<E>

  /**
   * Evaluate a block of assertions in the current nested context.
   *
   * @param subject the subject of the block of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   * @return an assertion for [subject].
   */
  fun <E> expect(
    subject: E,
    block: Asserter<E>.() -> Unit
  ): Asserter<E>

  /**
   * Evaluates a composed assertion on the original subject.
   * This creates a new assertion in the composed context using the same
   * subject as the overall assertion.
   * This is useful because it allows for the overall assertion to contain much
   * more detail in any failure message.
   *
   * @param description a description for the conditions the assertion evaluates.
   * @param assert the assertion implementation that should result in a call
   * to [Assertion.pass] or [Assertion.fail].
   * @return this assertion, in order to facilitate a fluent API.
   */
  override fun assert(
    description: String,
    assert: AtomicAssertion<T>.() -> Unit
  ): Asserter<T>

  /**
   * Evaluates a composed assertion on the original subject.
   * This creates a new assertion in the composed context using the same
   * subject as the overall assertion.
   * This is useful because it allows for the overall assertion to contain much
   * more detail in any failure message.
   *
   * @param description a description for the condition the assertion evaluates.
   * @param expected the expected value of a comparison.
   * @param assert the assertion implementation that should result in a call
   * to [Assertion.pass] or [Assertion.fail].
   * @return this assertion, in order to facilitate a fluent API.
   */
  override fun assert(
    description: String,
    expected: Any?,
    assert: AtomicAssertion<T>.() -> Unit
  ): Asserter<T>
}
