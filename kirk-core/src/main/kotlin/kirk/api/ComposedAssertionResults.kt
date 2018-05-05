package kirk.api

/**
 * The results of assertions made inside the block passed to
 * [AssertionContext.compose].
 */
interface ComposedAssertionResults {

  /**
   * A convenient way to chain a result handler after
   * [AssertionContext.compose].
   */
  infix fun results(block: ComposedAssertionResults.() -> Unit) = apply(block)

  /**
   * Report that the assertion succeeded.
   */
  fun pass()

  /**
   * Report that the assertion failed.
   */
  fun fail()

  /**
   * Returns `true` if any nested assertions evaluated inside
   * [AssertionContext.compose] failed, `false` otherwise.
   *
   * @see expect
   */
  val anyFailed: Boolean

  /**
   * Returns `true` if _all_ nested assertions evaluated using
   * [AssertionContext.compose] failed, `false` otherwise.
   *
   * @see expect
   */
  val allFailed: Boolean

  /**
   * Returns `true` if any nested assertions evaluated using
   * [AssertionContext.compose] passed, `false` otherwise.
   *
   * @see expect
   */
  val anyPassed: Boolean

  /**
   * Returns `true` if _all_ nested assertions evaluated using
   * [AssertionContext.compose] passed, `false` otherwise.
   *
   * @see expect
   */
  val allPassed: Boolean
}
