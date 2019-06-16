package strikt.assertions

import strikt.api.Assertion

/**
 * Asserts that the subject is a successful result and maps this assertion to
 * an assertion over the result value.
 *
 * @see [Result.isSuccess].
 */
fun <T : Any?> Assertion.Builder<Result<T>>.isSuccess() =
  assertThat("succeeded", Result<T>::isSuccess)
    .get("result value") { getOrNull() }

/**
 * Asserts that the subject is a failed result and maps this assertion to an
 * assertion over the exception that was thrown
 *
 * @see [Result.isFailure].
 */
fun <T : Any?> Assertion.Builder<Result<T>>.isFailure() =
  assertThat("failed with an exception", Result<T>::isFailure)
    .get("caught exception") {
      requireNotNull(exceptionOrNull())
    }
