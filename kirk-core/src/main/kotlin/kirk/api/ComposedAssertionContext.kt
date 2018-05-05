package kirk.api

/**
 * Allows assertions to be composed, or nested, using
 * [AssertionContext.compose].
 */
interface ComposedAssertionContext {
  /**
   * Start a chain of assertions in the current nested context.
   *
   * @param subject the subject of the chain of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @return an assertion for [subject].
   */
  fun <T> expect(subject: T): Assertion<T>

  /**
   * Evaluate a block of assertions in the current nested context.
   *
   * @param subject the subject of the block of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   * @return an assertion for [subject].
   */
  fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T>
}
