package strikt.assertions

import strikt.api.Assertion

fun <T : Map<K, V>, K, V> Assertion<T>.isEmpty() =
  passesIf("is empty") { isEmpty() }

operator fun <T : Map<K, V>, K, V> Assertion<T>.get(key: K): Assertion<V?> =
  map("entry [$key] %s") { get(key) }
