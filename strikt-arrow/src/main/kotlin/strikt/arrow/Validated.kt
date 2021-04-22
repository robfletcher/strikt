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
  assert("should be Valid") { subject ->
    subject.fold({ fail() }, { if (it == value) pass() else fail() })
  } as Assertion.Builder<Validated.Valid<A>>

/**
 * Unwraps the containing value of the [Validated.Valid]
 * @return Assertion builder over the unwrapped subject
 */
@Deprecated("Use value instead", replaceWith = ReplaceWith("value"))
val <A> Assertion.Builder<Validated.Valid<A>>.a: Assertion.Builder<A>
  get() = value

/**
 * Unwraps the containing value of the [Validated.Valid]
 * @return Assertion builder over the unwrapped subject
 * @see Validated.Valid.value
 */
val <A> Assertion.Builder<Validated.Valid<A>>.value: Assertion.Builder<A>
  @JvmName("validatedValidValue")
  get() = get("valid value", Validated.Valid<A>::value)

/**
 * Asserts that the [Validated] is [Validated.Invalid]
 * @return Assertion builder over the same subject that is now known to be
 * a [Validated.Invalid].
 */
@Suppress("UNCHECKED_CAST")
fun <E, A> Assertion.Builder<Validated<E, A>>.isInvalid() =
  assert("should be Invalid") { subject ->
    subject.fold({ pass() }, { fail() })
  } as Assertion.Builder<Validated.Invalid<E>>

/**
 * Asserts that the [Validated] is [Validated.Invalid] and that it contains the exact value
 * @param value Value to compare to the [Validated]'s wrapped value
 * @return Assertion builder over the same subject that is now known to be
 * a [Validated.Invalid].
 */
@Suppress("UNCHECKED_CAST")
infix fun <E, A> Assertion.Builder<Validated<E, A>>.isInvalid(value: E) =
  assert("should be Valid") { subject ->
    subject.fold({ if (it == value) pass() else fail() }, { fail() })
  } as Assertion.Builder<Validated.Invalid<E>>

/**
 * Unwraps the containing value of the [Validated.Invalid]
 * @return Assertion builder over the unwrapped subject
 */
@Deprecated("Use value instead", replaceWith = ReplaceWith("value"))
val <E> Assertion.Builder<Validated.Invalid<E>>.e: Assertion.Builder<E>
  get() = value

/**
 * Unwraps the containing value of the [Validated.Invalid]
 * @return Assertion builder over the unwrapped subject
 * @see Validated.Invalid.value
 */
val <E> Assertion.Builder<Validated.Invalid<E>>.value: Assertion.Builder<E>
  @JvmName("validatedInvalidValue")
  get() = get("invalid value", Validated.Invalid<E>::value)
