package strikt.api

/**
 * An assertion of a single condition on a subject.
 */
interface AtomicAssertion : Assertion {
  /**
   * Mark this result as passed due to a comparison between two values.
   *
   * @param actual an actual value, that is the value that matched
   * the expected value.
   * @param description A description of the assertion. May contain a
   * [String.format] style placeholder for the [actual] value.
   */
  fun pass(
    actual: Any?,
    description: String? = null
  )

  /**
   * Mark this result as failed due to a comparison between two values.
   *
   * @param actual an actual value, that is the value that differed
   * from the expected value.
   * @param description A description of the failure. May contain a
   * [String.format] style placeholder for the [actual] value.
   * @property cause The exception that caused the failure, if any.
   */
  fun fail(
    actual: Any?,
    description: String? = null,
    cause: Throwable? = null
  )
}
