package strikt.api

/**
 * A description of an individual assertion failure.
 *
 * @property actual The actual value, if relevant to the assertion.
 * @property message A description of the failure.
 * @property cause The exception that caused the failure, if any.
 */
data class Failure(
  val actual: Any? = null,
  val message: String? = null,
  val cause: Throwable? = null
)
