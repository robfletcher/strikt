package strikt.assertions

import strikt.api.Assertion

/**
 * Asserts that the subject is a successful result and maps this assertion to
 * an assertion over the result value.
 *
 * @see [Result.isSuccess].
 */
@Deprecated(
  message = "Replaced with isSuccess()",
  replaceWith = ReplaceWith("isSuccess()")
)
fun <T : Any?> Assertion.Builder<Result<T>>.succeeded(): Assertion.Builder<T> =
  isSuccess()

/**
 * Asserts that the subject is a failed result and maps this assertion to an
 * assertion over the exception that was thrown
 *
 * @see [Result.isFailure].
 */
@Deprecated(
  message = "Replaced with isFailure()",
  replaceWith = ReplaceWith("isFailure()")
)
fun <T : Any?> Assertion.Builder<Result<T>>.failed(): Assertion.Builder<Throwable> =
  isFailure()

/**
 * Asserts that the subject is a isFailure result that threw an exception
 * assignable to [E] and maps this assertion to an assertion over that
 * exception.
 *
 * @see [Result.isFailure].
 */
inline fun <reified E : Throwable> Assertion.Builder<Result<*>>.failedWith() =
  isFailure().isA<E>()
