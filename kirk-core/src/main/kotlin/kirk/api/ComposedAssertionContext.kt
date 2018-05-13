package kirk.api

interface ComposedAssertionContext {
  fun pass()
  fun fail()
  val allPassed: Boolean
  val anyPassed: Boolean
  val allFailed: Boolean
  val anyFailed: Boolean

  /**
   * A convenient way to handle to composed assertion results.
   *
   * @see AssertionContext.compose
   * // TODO: sample here
   */
  infix fun then(block: ComposedAssertionContext.() -> Unit) {
    this.block()
  }
}