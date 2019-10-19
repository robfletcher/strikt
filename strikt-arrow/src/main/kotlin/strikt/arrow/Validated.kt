package strikt.arrow

import arrow.core.Validated
import strikt.api.Assertion

@Suppress("UNCHECKED_CAST")
fun <E, A> Assertion.Builder<Validated<E, A>>.isValid() =
  assert("should be Valid") {
    it.fold({ fail() }, { pass() })
  } as Assertion.Builder<Validated.Valid<A>>


@Suppress("UNCHECKED_CAST")
fun <E, A> Assertion.Builder<Validated<E, A>>.isValid(value: A) =
  assert("should be Valid") {
    it.fold({ fail() }, { if (it == value) pass() else fail() })
  } as Assertion.Builder<Validated.Valid<A>>

val <A> Assertion.Builder<Validated.Valid<A>>.a: Assertion.Builder<A>
  @JvmName("validatedValid")
  get() = get("valid value") { a }

@Suppress("UNCHECKED_CAST")
fun <E, A> Assertion.Builder<Validated<E, A>>.isInvalid() =
  assert("should be Invalid") {
    it.fold({ pass() }, { fail() })
  } as Assertion.Builder<Validated.Invalid<E>>

@Suppress("UNCHECKED_CAST")
fun <E, A> Assertion.Builder<Validated<E, A>>.isInvalid(value: E) =
  assert("should be Valid") {
    it.fold({ if (it == value) pass() else fail() }, { fail() })
  } as Assertion.Builder<Validated.Invalid<E>>

val <E> Assertion.Builder<Validated.Invalid<E>>.e: Assertion.Builder<E>
  @JvmName("validatedInvalid")
  get() = get("invalid value") { e }
