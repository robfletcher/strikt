package strikt.arrow

import arrow.core.Validated
import strikt.api.Assertion

/**
 * Asserts that the [Validated] is [Validated.Valid]
 * @return Assertion builder over the same subject that is now known to be
 * a [Validated.Valid].
 */
@Suppress("UNCHECKED_CAST")
fun <E, A> Assertion.Builder<Validated<E, A>>.isValid() =
  assert("should be Valid") {
    it.fold({ fail() }, { pass() })
  } as Assertion.Builder<Validated.Valid<A>>

/**
 * Asserts that the [Validated] is [Validated.Valid] and that it contains the exact value
 * @param value Value to compare to the [Validated]'s wrapped value
 * @return Assertion builder over the same subject that is now known to be
 * a [Validated.Valid].
 */
@Suppress("UNCHECKED_CAST")
infix fun <E, A> Assertion.Builder<Validated<E, A>>.isValid(value: A) =
  assert("should be Valid") {
    it.fold({ fail() }, { if (it == value) pass() else fail() })
  } as Assertion.Builder<Validated.Valid<A>>

/**
 * Unwraps the containing value of the [Validated.Valid]
 * @return Assertion builder over the unwrapped subject
 */
val <A> Assertion.Builder<Validated.Valid<A>>.a: Assertion.Builder<A>
  @JvmName("validatedValid")
  get() = get("valid value") { a }

/**
 * Asserts that the [Validated] is [Validated.Invalid]
 * @return Assertion builder over the same subject that is now known to be
 * a [Validated.Invalid].
 */
@Suppress("UNCHECKED_CAST")
fun <E, A> Assertion.Builder<Validated<E, A>>.isInvalid() =
  assert("should be Invalid") {
    it.fold({ pass() }, { fail() })
  } as Assertion.Builder<Validated.Invalid<E>>

/**
 * Asserts that the [Validated] is [Validated.Invalid] and that it contains the exact value
 * @param value Value to compare to the [Validated]'s wrapped value
 * @return Assertion builder over the same subject that is now known to be
 * a [Validated.Invalid].
 */
@Suppress("UNCHECKED_CAST")
infix fun <E, A> Assertion.Builder<Validated<E, A>>.isInvalid(value: E) =
  assert("should be Valid") {
    it.fold({ if (it == value) pass() else fail() }, { fail() })
  } as Assertion.Builder<Validated.Invalid<E>>

/**
 * Unwraps the containing value of the [Validated.Invalid]
 * @return Assertion builder over the unwrapped subject
 */
val <E> Assertion.Builder<Validated.Invalid<E>>.e: Assertion.Builder<E>
  @JvmName("validatedInvalid")
  get() = get("invalid value") { e }
