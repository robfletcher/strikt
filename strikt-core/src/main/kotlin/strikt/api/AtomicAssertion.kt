package strikt.api

/**
 * An assertion of a single condition on a subject.
 */
interface AtomicAssertion : Assertion {
  /**
   * Mark this result as failed due to a comparison between two values.
   *
   * @param expected an expected value.
   * @param actual an actual value, that is the value that differed
   * from [expected].
   * @param description A description of the failure. May contain a
   * [String.format] style placeholder for the [actual] value.
   * @property cause The exception that caused the failure, if any.
   */
  fun fail(
    expected: Any?,
    actual: Any?,
    description: String? = "found %s",
    cause: Throwable? = null
  )
}
