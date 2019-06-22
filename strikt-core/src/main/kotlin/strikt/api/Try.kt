package strikt.api

sealed class Try<out T : Any?>

class Success<T> internal constructor(val value: T) : Try<T>()

class Failure internal constructor(val exception: Throwable) : Try<Nothing>()

internal fun <T : Any?> tryCatching(block: () -> T): Try<T> {
  return try {
    Success(block())
  } catch (e: Throwable) {
    Failure(e)
  }
}
