package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Maps an assertion on an enum to an assertion on its name.
 *
 * @see Enum.name
 */
val <T : Enum<T>> Builder<T>.name: Builder<String>
  get() = get(Enum<T>::name)

/**
 * Maps an assertion on an enum to an assertion on its ordinal.
 *
 * @see Enum.ordinal
 */
val <T : Enum<T>> Builder<T>.ordinal: Builder<Int>
  get() = get(Enum<T>::ordinal)

/**
 * Asserts that the subject is one of [values].
 */
fun <T : Enum<T>> Builder<T>.isOneOf(vararg values: T): Builder<T> =
  assert("is one of ${values.joinToString()}") {
    if (it in values) {
      pass(it)
    } else {
      fail(it)
    }
  }
