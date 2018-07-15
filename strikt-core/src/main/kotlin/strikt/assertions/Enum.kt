package strikt.assertions

import strikt.api.Assertion

/**
 * Maps an assertion on an enum to an assertion on its name.
 *
 * @see Enum.name
 */
val <T : Enum<T>> Assertion<T>.name: Assertion<String>
  get() = map(Enum<T>::name)

/**
 * Maps an assertion on an enum to an assertion on its ordinal.
 *
 * @see Enum.ordinal
 */
val <T : Enum<T>> Assertion<T>.ordinal: Assertion<Int>
  get() = map(Enum<T>::ordinal)
