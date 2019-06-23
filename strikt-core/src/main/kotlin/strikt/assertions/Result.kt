package strikt.assertions

import strikt.api.Assertion
import strikt.api.Failure
import strikt.api.Success
import strikt.api.Try

/**
 * Asserts that the subject is a successful result and maps this assertion to
 * an assertion over the result value.
 *
 * @see [Result.isSuccess].
 */
fun <T : Any?> Assertion.Builder<Try<T>>.succeeded(): Assertion.Builder<T> {
  @Suppress("UNCHECKED_CAST")
  return assert("succeeded") { subject ->
    when (subject) {
      is Success -> pass()
      is Failure -> fail(
        description = "threw %s",
        actual = subject.exception,
        cause = subject.exception
      )
    }
  }.get("returned value") { (this as Success<T>).value }
}

/**
 * Asserts that the subject is a failed result and maps this assertion to an
 * assertion over the exception that was thrown
 *
 * @see [Result.isFailure].
 */
fun <T : Any?> Assertion.Builder<Try<T>>.failed(): Assertion.Builder<Throwable> {
  @Suppress("UNCHECKED_CAST")
  return assert("failed with an exception") { subject ->
    when (subject) {
      is Success -> fail(
        description = if (subject.value is Unit) "ran successfully" else "returned %s",
        actual = subject.value
      )
      is Failure -> pass()
    }
  }.get("caught exception") { (this as Failure).exception }
}

/**
 * Asserts that the subject is a isFailure result that threw an exception
 * assignable to [E] and maps this assertion to an assertion over that
 * exception.
 *
 * @see [Result.isFailure].
 */
inline fun <reified E : Throwable> Assertion.Builder<Try<*>>.failedWith() =
  failed().isA<E>()
