package strikt.assertions

import strikt.api.Assertion.Builder
import strikt.internal.reporting.formatValue

/**
 * Asserts that the subject map is empty.
 */
fun <T : Map<K, V>, K, V> Builder<T>.isEmpty() =
  assertThat("is empty", Map<K, V>::isEmpty)

/**
 * Asserts that the subject map is not empty.
 */
fun <T : Map<K, V>, K, V> Builder<T>.isNotEmpty() =
  assertThat("is not empty", Map<K, V>::isNotEmpty as (Map<K, V>) -> Boolean)

/**
 * Asserts that the subject map has the specified number of entries.
 */
infix fun <T : Map<K, V>, K, V> Builder<T>.hasSize(expected: Int) =
  assert("has size %d", expected) {
    when (it.size) {
      expected -> pass(actual = it.size)
      else -> fail(actual = it.size)
    }
  }

/**
 * Maps this assertion to an assertion on the value indexed by [key] in the
 * subject list.
 *
 * @return An assertion on the value indexed by [key] or `null` if no such entry
 * exists in the subject map.
 */
operator fun <T : Map<K, V>, K, V> Builder<T>.get(key: K): Builder<V?> =
  get("entry [${formatValue(key)}]") { this[key] }

/**
 * Asserts that [key] exists in the subject map and then maps this assertion to
 * an assertion on the associated value.
 *
 * @return An assertion on the value indexed by [key].
 */
infix fun <T : Map<K, V>, K, V> Builder<T>.getValue(key: K): Builder<V> =
  containsKey(key)
    .get("entry [${formatValue(key)}]") {
      this.getValue(key)
    }

/**
 * Runs a group of assertions on the value in the subject map tht corresponds to [key].
 *
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return this builder, to facilitate chaining.
 */
fun <T : Map<K, V>, K, V> Builder<T>.withValue(
  key: K,
  block: Builder<V>.() -> Unit
): Builder<T> =
  with("entry [${formatValue(key)}]", { getValue(key) }, block)

/**
 * Asserts that the subject map contains an entry indexed by [key]. Depending on
 * the map implementation the value associated with [key] may be `null`. This
 * assertion just tests for the existence of the key.
 */
infix fun <T : Map<K, V>, K, V> Builder<T>.containsKey(key: K): Builder<T> =
  assertThat("has an entry with the key %s", key) {
    it.containsKey(key)
  }

/**
 * Asserts that the subject map does not contain an entry indexed by [key]. Depending on
 * the map implementation the value associated with [key] may be `null`. This
 * assertion just tests for the nonexistence of the key.
 */
infix fun <T : Map<K, V>, K, V> Builder<T>.doesNotContainKey(key: K): Builder<T> =
  assertThat("does not have an entry with the key %s", key) {
    !it.containsKey(key)
  }

/**
 * Asserts that the subject map contains entries for all [keys].
 */
fun <T : Map<K, V>, K, V> Builder<T>.containsKeys(vararg keys: K): Builder<T> =
  compose("has entries with the keys %s", keys.toList()) {
    keys.forEach { key -> containsKey(key) }
  } then {
    if (allPassed) pass() else fail()
  }

/**
 * Asserts that the subject map doesn't contain entries for all [keys].
 */
fun <T : Map<K, V>, K, V> Builder<T>.doesNotContainKeys(vararg keys: K): Builder<T> =
  compose("doesn't have entries with the keys %s", keys.toList()) {
    keys.forEach { key -> doesNotContainKey(key) }
  } then {
    if (allPassed) pass() else fail()
  }

/**
 * Asserts that the subject map contains an entry indexed by [key] with a value
 * equal to [value].
 */
fun <T : Map<K, V>, K, V> Builder<T>.hasEntry(
  key: K,
  value: V
): Builder<T> =
  apply { containsKey(key)[key].isEqualTo(value) }
