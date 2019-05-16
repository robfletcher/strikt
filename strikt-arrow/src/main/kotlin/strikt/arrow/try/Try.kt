package strikt.arrow.`try`

import arrow.core.Failure
import arrow.core.Predicate
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

@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Try<T>>.isSuccessWhere(check: Predicate<T>) =
    assert("should be Success") {
        when (it) {
            is Success -> {
                if (check(it.value)) {
                    pass()
                } else {
                    fail()
                }
            }
            else -> fail()
        }
    } as Assertion.Builder<Success<T>>

@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Try<T>>.isFailure() =
    assert("should be Failure") {
        when (it) {
            is Failure -> pass()
            else -> fail()
        }
    } as Assertion.Builder<Failure>


@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Try<T>>.isFailure(e: Exception) =
    assert("should be Failure") {
        when (it) {
            is Failure -> {
                if (it.exception == e)
                    pass()
                else
                    fail(actual = it.exception)
            }
            else -> fail()
        }
    } as Assertion.Builder<Failure>

@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Try<T>>.isFailureWhere(check: Predicate<Throwable>) =
    assert("should be Failure") {
        when (it) {
            is Failure -> {
                if (check(it.exception)) {
                    pass()
                } else {
                    fail()
                }
            }
            else -> fail()
        }
    } as Assertion.Builder<Failure>


inline fun <reified E : Throwable> Assertion.Builder<Try<*>>.isFailureOfType() =
    isFailureWhere {
        when (it) {
            is E -> true
            else -> false
        }
    }
