package strikt.assertions

import strikt.api.Assertion
import strikt.api.DescribeableBuilder

/**
 * Asserts that the subject is a successful result and maps this assertion to
 * an assertion over the result value.
 *
 * @see [Result.isSuccess].
 */
fun <T : Any?> Assertion.Builder<Result<T>>.succeeded(): DescribeableBuilder<T> =
  assertThat("succeeded", Result<T>::isSuccess)
    .get("result value") {
      getOrElse {
        error("Result was successful but failed to return a value")
      }
    }

/**
 * Asserts that the subject is a failed result and maps this assertion to an
 * assertion over the exception that was thrown
 *
 * @see [Result.isFailure].
 */
fun <T : Any?> Assertion.Builder<Result<T>>.failed(): DescribeableBuilder<Throwable> =
  assertThat("failed with an exception", Result<T>::isFailure)
    .get("caught exception") {
      requireNotNull(exceptionOrNull())
    }

/**
 * Asserts that the subject is a isFailure result that threw an exception
 * assignable to [E] and maps this assertion to an assertion over that
 * exception.
 *
 * @see [Result.isFailure].
 */
inline fun <reified E : Throwable> Assertion.Builder<Result<*>>.failedWith() =
  failed().isA<E>()
