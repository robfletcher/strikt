package strikt.api

data class Failure(
  val actual: Any? = null,
  val message: String? = null,
  val cause: Throwable? = null
)