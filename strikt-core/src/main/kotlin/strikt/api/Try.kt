package strikt.api

/**
 * Represents the outcome of an action that may return a value of type [T] or
 * throw an exception.

 * In the case of a successful outcome [T] is the declared (or inferred) return
 * type of the action. If [T] is [Unit] the action returned no value, but
 * completed successfully. [T] may be a nullable type if `null` is a legitimate
 * return value for the action.
 *
 * In the case of a failed outcome [T] is [Nothing] as the action did not return
 * normally.
 */
sealed class Try<out T : Any?>

internal data class Success<out T : Any?>(
  val value: T
) : Try<T>() {
  override fun toString() = "Success($value)"
}

internal class Failure(
  val exception: Throwable
) : Try<Nothing>() {
  override fun toString() =
    "Failure(${exception.javaClass.simpleName}: ${exception.message})"
}

internal fun <T : Any?> tryCatching(block: () -> T): Try<T> {
  return try {
    Success(block())
  } catch (e: Throwable) {
    Failure(e)
  }
}
