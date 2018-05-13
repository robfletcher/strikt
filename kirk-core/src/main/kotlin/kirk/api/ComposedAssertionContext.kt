package kirk.api

/**
 * Allows the containing assertion to make decisions about whether it has passed
 * or failed based on the results of composed assertions.
 *
 * @see AssertionContext.compose
 */
interface ComposedAssertionContext {
  fun pass()
  fun fail()
  val allPassed: Boolean
  val anyPassed: Boolean
  val allFailed: Boolean
  val anyFailed: Boolean

  /**
   * A convenient way to handle to composed assertion results.
   * Chain a call to this method after [AssertionContext.compose] in order to
   * handle the composed assertion results.
   *
   * @see AssertionContext.compose
   * // TODO: sample here
   */
  infix fun then(block: ComposedAssertionContext.() -> Unit) {
    this.block()
  }
}