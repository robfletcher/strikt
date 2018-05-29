package strikt.assertions

import strikt.api.Assertion

fun <T : Map<K, V>, K, V> Assertion<T>.isEmpty() =
  passesIf("is empty") { isEmpty() }