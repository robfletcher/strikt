package strikt.assertions

import strikt.api.Assertion

fun <T : Any> Assertion.Builder<Result<T>>.isSuccess() =
  assertThat("is successful", Result<T>::isSuccess)
    .get {
      requireNotNull(getOrNull())
    }

fun <T : Any> Assertion.Builder<Result<T>>.isFailure() =
  assertThat("is successful", Result<T>::isFailure)
    .get {
      requireNotNull(exceptionOrNull())
    }
