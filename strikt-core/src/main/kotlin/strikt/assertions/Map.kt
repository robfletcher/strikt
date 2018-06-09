package strikt.assertions

import strikt.api.Assertion

fun <T : Map<K, V>, K, V> Assertion<T>.isEmpty() =
  passesIf("is empty") { isEmpty() }

operator fun <T : Map<K, V>, K, V> Assertion<T>.get(key: K): Assertion<V?> =
  map("entry [$key] %s") { get(key) }

fun <K, V> Assertion<Map<K, V>>.hasEntry(key: K): Assertion<V> =
  assert("has an entry with the key %s", key) {
    if (subject.containsKey(key)) pass() else fail()
  }[key] as Assertion<V>
