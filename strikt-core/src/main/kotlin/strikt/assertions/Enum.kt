package strikt.assertions

import strikt.api.Assertion

val <T : Enum<T>> Assertion<T>.name: Assertion<String>
  get() = map(Enum<T>::name)

val <T : Enum<T>> Assertion<T>.ordinal: Assertion<Int>
  get() = map(Enum<T>::ordinal)
