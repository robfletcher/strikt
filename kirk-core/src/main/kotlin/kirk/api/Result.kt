package kirk.api

/**
 * Represents the result of an assertion or a group of assertions.
 */
data class Result
internal constructor(
  /**
   * The status of the result.
   */
  val status: Status,
  /**
   * The description of the assertion as passed to [Assertion.assert].
   */
  val description: String,
  /**
   * The subject value of the assertion.
   */
  val subject: Any?,
  /**
   * The actual value or values that violated the assertion.
   * This property is optional as it does not make sense for all types of
   * assertion.
   * However, it can help improve diagnostic messages where it _is_ appropriate.
   *
   * @see AssertionContext.fail
   * @see ComposedAssertions.fail
   */
  val actual: Any? = null,
  /**
   * Contains the results of any nested assertions.
   */
  val nestedResults: Collection<Result> = emptyList()
) {
  /**
   * The number of assertions evaluated against [subject]
   */
  val assertionCount: Int = when (nestedResults.isEmpty()) {
    true  -> 1
    false -> nestedResults.size
  }
  /**
   * The number of assertions that passed.
   */
  val passCount: Int = when (nestedResults.isEmpty()) {
    true  -> if (status == Status.Passed) 1 else 0
    false -> nestedResults.sumBy { it.passCount }
  }
  /**
   * The number of assertions that failed.
   */
  val failureCount: Int = when (nestedResults.isEmpty()) {
    true  -> if (status == Status.Failed) 1 else 0
    false -> nestedResults.sumBy { it.failureCount }
  }
}
