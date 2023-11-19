@file:Suppress("DEPRECATION")

package strikt.arrow

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import strikt.api.Assertion

/**
 * Asserts that the [Option] is [None]
 * @return Assertion builder over the same subject that is now known to be
 * a [None].
 */
@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Option<T>>.isNone() =
  assert("should be None") {
    when (it) {
      is Some -> fail()
      is None -> pass()
    }
  } as Assertion.Builder<None>

/**
 * Asserts that the [Option] is [Some]
 * @return Assertion builder over the same subject that is now known to be
 * a [Some].
 */
@Suppress("UNCHECKED_CAST")
fun <T> Assertion.Builder<Option<T>>.isSome() =
  assert("should be Some") {
    when (it) {
      is Some -> pass()
      is None -> fail()
    }
  } as Assertion.Builder<Some<T>>

/**
 * Asserts that the [Option] is [Some] and that it contains the exact value
 * @param value Value to compare to the [Option]'s wrapped value
 * @return Assertion builder over the same subject that is now known to be
 * a [Some].
 */
@Suppress("UNCHECKED_CAST")
infix fun <T> Assertion.Builder<Option<T>>.isSome(value: T) =
  assert("should be Some($value)") {
    when (it) {
      is Some -> {
        if (it.value == value) {
          pass()
        } else {
          fail()
        }
      }
      is None -> fail()
    }
  } as Assertion.Builder<Some<T>>

/**
 * Unwraps the containing value of the [Some]
 * @return Assertion builder over the unwrapped subject
 */
@Deprecated("Use value instead", replaceWith = ReplaceWith("value"))
val <T> Assertion.Builder<Some<T>>.t: Assertion.Builder<T>
  get() = value

/**
 * Unwraps the containing value of the [Some]
 * @return Assertion builder over the unwrapped subject
 */
val <T> Assertion.Builder<Some<T>>.value: Assertion.Builder<T>
  get() = get("some value", Some<T>::value)
