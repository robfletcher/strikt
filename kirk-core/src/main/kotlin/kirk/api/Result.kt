package kirk.api

import java.io.Writer

/**
 * Represents the result of an assertion or a group of assertions.
 */
interface Result {
  /**
   * The status of the result.
   */
  val status: Status
  /**
   * The description of the assertion as passed to [Assertion.assert].
   */
  val description: String
  /**
   * The subject value of the assertion.
   */
  val subject: Any?
  /**
   * The number of assertions evaluated against [subject]
   */
  val assertionCount: Int
  /**
   * The number of assertions that passed.
   */
  val passCount: Int
  /**
   * The number of assertions that failed.
   */
  val failureCount: Int

  // TODO: these should be internal
  /**
   * Outputs the result details for logging purposes.
   */
  fun describeTo(writer: Writer, indent: Int)

  /**
   * Outputs the result details for logging purposes.
   */
  fun describeTo(writer: Writer) {
    describeTo(writer, 0)
  }
}

/**
 * The basic state of an assertion result.
 */
enum class Status {
  /**
   * The assertion passed.
   */
  Passed,
  /**
   * The assertion failed.
   */
  Failed
  // TODO: may want an `Error` too
}

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
