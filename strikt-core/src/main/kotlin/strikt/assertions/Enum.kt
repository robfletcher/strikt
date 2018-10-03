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
