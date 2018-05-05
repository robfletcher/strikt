package kirk.api

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
}
