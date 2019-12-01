package strikt.arrow

import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import strikt.api.Assertion

/**
 * Asserts that the [Try] is [Success]
 * @return Assertion builder over the same subject that is now known to be
 * a [Success].
 */
@Suppress("UNCHECKED_CAST", "DEPRECATION")
fun <T> Assertion.Builder<Try<T>>.isSuccess() =
  assert("should be Success") {
    when (it) {
      is Success -> pass()
      else -> fail()
    }
  } as Assertion.Builder<Success<T>>

/**
 * Asserts that the [Try] is [Success] and that it contains the exact value
 * @param value Value to compare to the [Try]'s wrapped value
 * @return Assertion builder over the same subject that is now known to be
 * a [Success].
 */
@Suppress("UNCHECKED_CAST", "DEPRECATION")
infix fun <T> Assertion.Builder<Try<T>>.isSuccess(value: T) =
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

/**
 * Unwraps the containing value of the [Success]
 * @return Assertion builder over the unwrapped subject
 */
@Suppress("DEPRECATION")
val <T> Assertion.Builder<Success<T>>.value: Assertion.Builder<T>
  get() = get("success value") { value }

/**
 * Asserts that the [Try] is [Failure]
 * @return Assertion builder over the same subject that is now known to be
 * a [Failure].
 */
@Suppress("UNCHECKED_CAST", "DEPRECATION")
fun <T> Assertion.Builder<Try<T>>.isFailure() =
  assert("should be Failure") {
    when (it) {
      is Failure -> pass()
      else -> fail()
    }
  } as Assertion.Builder<Failure>

/**
 * Unwraps the containing [Throwable] value of the [Failure]
 * @return Assertion builder over the unwrapped [Throwable]
 */
@Suppress("DEPRECATION")
val Assertion.Builder<Failure>.exception: Assertion.Builder<Throwable>
  get() = get("failure exception") { exception }
