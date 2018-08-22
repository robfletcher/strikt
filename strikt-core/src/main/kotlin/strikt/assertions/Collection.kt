package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Asserts that the subject has a [Collection.size] of exactly [expected].
 */
fun <T : Collection<E>, E> Builder<T>.hasSize(expected: Int): Builder<T> =
  assert("has size %d", expected) {
    when (it.size) {
      expected -> pass()
      else -> fail(expected = expected, actual = it.size)
    }
  }

/**
 * Asserts that the subject collection is empty.
 */
fun <T : Collection<E>, E> Builder<T>.isEmpty(): Builder<T> =
  passesIf("is empty", Collection<E>::isEmpty)

/**
 * Asserts that the subject collection is _not_ empty.
 */
fun <T : Collection<E>, E> Builder<T>.isNotEmpty(): Builder<T> =
  passesIf("is not empty", Collection<E>::isNotEmpty)

/**
 * Maps an assertion on a collection to an assertion on its size.
 *
 * @see Collection.size
 */
val <T : Collection<*>> Builder<T>.size: Builder<Int>
  get() = map(Collection<*>::size)
