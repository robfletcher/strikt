package strikt.api

/**
 * Allows the containing assertion to make decisions about whether it has passed
 * or failed based on the results of composed assertions.
 *
 * @see Assertion.compose
 * @property allPassed `true` if all composed assertions passed, otherwise
 * `false`.
 * @property anyPassed `true` if at least one composed assertion passed,
 * otherwise `false`.
 * @property allFailed `true` if all composed assertions failed, otherwise
 * `false`.
 * @property anyFailed `true` if at least one composed assertion failed,
 * otherwise `false`.
 */
interface ComposedAssertionContext<T> {
  /**
   * Report that the composed assertion succeeded.
   */
  fun pass()

  /**
   * Report that the composed assertion failed.
   */
  fun fail()

  val allPassed: Boolean
  val anyPassed: Boolean
  val allFailed: Boolean
  val anyFailed: Boolean

  /**
   * A convenient way to handle to composed assertion results.
   * Chain a call to this method after [Assertion.compose] in order to handle
   * the composed assertion results.
   *
   * @see Assertion.compose
   * // TODO: sample here
   */
  infix fun then(block: ComposedAssertionContext<T>.() -> Unit): Assertion<T>
}
