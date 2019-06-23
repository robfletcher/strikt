package strikt.api

/**
 * Represents the result of an action that may return a value of type [T] or
 * throw an exception.
 */
sealed class Try<out T : Any?>

/**
 * Represents the outcome of an action that completed normally and returned a
 * value of type [T].
 *
 * If [T] is [Unit] this represents the successful execution of an action
 * that returned no value.
 */
data class Success<out T : Any?>
internal constructor(
  /**
   * The value returned by the action. May be `null` for actions that
   * legitimately return `null` or [Unit] in the case of actions that return no
   * value at all.
   */
  val value: T
) : Try<T>() {
  override fun toString() = "Success($value)"
}

/**
 * Represents the failed outcome of an action that threw an exception.
 */
data class Failure
internal constructor(
  /**
   * The exception thrown by a failed action.
   */
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
