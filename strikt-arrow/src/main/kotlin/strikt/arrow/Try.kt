package strikt.arrow

import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import strikt.api.Assertion

@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Try<T>>.isSuccess() =
  assert("should be Success") {
    when (it) {
      is Success -> pass()
      else -> fail()
    }
  } as Assertion.Builder<Success<T>>

@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Try<T>>.isSuccess(value: T) =
  assert("should be Success($value)") {
    when (it) {
      is Success -> {
        if (it.value == value) {
          pass()
        } else {
          fail()
        }
      }
      else -> fail()
    }
  } as Assertion.Builder<Success<T>>

val <T> Assertion.Builder<Success<T>>.value: Assertion.Builder<T>
  get() = get("success value") { value }

@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Try<T>>.isFailure() =
  assert("should be Failure") {
    when (it) {
      is Failure -> pass()
      else -> fail()
    }
  } as Assertion.Builder<Failure>

val Assertion.Builder<Failure>.exception: Assertion.Builder<Throwable>
  get() = get("failure exception") { exception }
