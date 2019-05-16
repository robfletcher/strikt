package strikt.arrow.option

import arrow.core.None
import arrow.core.Option
import arrow.core.Predicate
import arrow.core.Some
import strikt.api.Assertion

@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Option<T>>.isNone() =
    assert("should be None") {
        when (it) {
            is Some -> fail()
            is None -> pass()
        }
    } as Assertion.Builder<None>

@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Option<T>>.isSome() =
    assert("should be Some") {
        when (it) {
            is Some -> pass()
            is None -> fail()
        }
    } as Assertion.Builder<Some<T>>

@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Option<T>>.isSome(value: T) =
    assert("should be Some($value)") {
        when (it) {
            is Some -> {
                if (it.t == value) {
                    pass()
                } else {
                    fail()
                }
            }
            is None -> fail()
        }
    } as Assertion.Builder<Some<T>>

@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Option<T>>.isSomeWhere(predicate: Predicate<T>) =
    assert("should be Some") {
        when (it) {
            is Some -> {
                if (predicate(it.t)) {
                    pass()
                } else {
                    fail()
                }
            }
            is None -> fail()
        }
    } as Assertion.Builder<Some<T>>
