package strikt.assertions

import strikt.api.Asserter

/**
 * Maps an assertion on an enum to an assertion on its name.
 *
 * @see Enum.name
 */
val <T : Enum<T>> Asserter<T>.name: Asserter<String>
  get() = map(Enum<T>::name)

/**
 * Maps an assertion on an enum to an assertion on its ordinal.
 *
 * @see Enum.ordinal
 */
val <T : Enum<T>> Asserter<T>.ordinal: Asserter<Int>
  get() = map(Enum<T>::ordinal)
