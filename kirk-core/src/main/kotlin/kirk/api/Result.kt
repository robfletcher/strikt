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
