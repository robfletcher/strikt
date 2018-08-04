package strikt.assertions

import strikt.api.Asserter
import strikt.internal.reporting.formatValue

/**
 * Asserts that the subject map is empty.
 */
fun <T : Map<K, V>, K, V> Asserter<T>.isEmpty() =
  passesIf("is empty") { isEmpty() }

/**
 * Maps this assertion to an assertion on the value indexed by [key] in the
 * subject list.
 *
 * @return An assertion on the value indexed by [key] or `null` if no such entry
 * exists in the subject map.
 */
operator fun <T : Map<K, V>, K, V> Asserter<T>.get(key: K): Asserter<V?> =
  map("entry [${formatValue(key)}] %s") { get(key) }

/**
 * Asserts that the subject map contains an entry indexed by [key]. Depending on
 * the map implementation the value associated with [key] may be `null`. This
 * assertion just tests for the existence of the key.
 */
fun <T : Map<K, V>, K, V> Asserter<T>.containsKey(key: K): Asserter<T> =
  passesIf("has an entry with the key %s", key) {
    containsKey(key)
  }

/**
 * Asserts that the subject map contains entries for all [keys].
 */
fun <T : Map<K, V>, K, V> Asserter<T>.containsKeys(vararg keys: K): Asserter<T> =
  assertAll {
    keys.forEach { containsKey(it) }
  }

/**
 * Asserts that the subject map contains an entry indexed by [key] with a value
 * equal to [value].
 */
fun <T : Map<K, V>, K, V> Asserter<T>.hasEntry(
  key: K,
  value: V
): Asserter<T> =
  apply { containsKey(key)[key].isEqualTo(value) }
