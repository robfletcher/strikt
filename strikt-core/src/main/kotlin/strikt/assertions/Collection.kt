package strikt.assertions

import strikt.api.Assertion

/**
 * Asserts that the subject has a [Collection.size] of exactly [expected].
 */
fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  assert("has size %d", expected) {
    when (subject.size) {
      expected -> pass()
      else -> fail(actual = subject.size)
    }
  }

/**
 * Asserts that the subject collection is empty.
 */
fun <T : Collection<E>, E> Assertion<T>.isEmpty(): Assertion<T> =
  passesIf("is empty", Collection<E>::isEmpty)

/**
 * Asserts that the subject collection is _not_ empty.
 */
fun <T : Collection<E>, E> Assertion<T>.isNotEmpty(): Assertion<T> =
  passesIf("is not empty", Collection<E>::isNotEmpty)

/**
 * Maps an assertion on a collection to an assertion on its size.
 *
 * @see Collection.size
 */
val <T : Collection<*>> Assertion<T>.size: Assertion<Int>
  get() = map(Collection<*>::size)
