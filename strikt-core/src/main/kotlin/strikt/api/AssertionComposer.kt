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
  fun <E> expect(subject: E): DescribeableAsserter<E>

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
  ): DescribeableAsserter<E>
}
