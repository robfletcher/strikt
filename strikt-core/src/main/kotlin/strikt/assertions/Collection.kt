package strikt.assertions

import strikt.api.Asserter

/**
 * Asserts that the subject has a [Collection.size] of exactly [expected].
 */
fun <T : Collection<E>, E> Asserter<T>.hasSize(expected: Int): Asserter<T> =
  assert("has size %d", expected) {
    when (subject.size) {
      expected -> pass()
      else -> fail(actual = subject.size)
    }
  }

/**
 * Asserts that the subject collection is empty.
 */
fun <T : Collection<E>, E> Asserter<T>.isEmpty(): Asserter<T> =
  passesIf("is empty", Collection<E>::isEmpty)

/**
 * Asserts that the subject collection is _not_ empty.
 */
fun <T : Collection<E>, E> Asserter<T>.isNotEmpty(): Asserter<T> =
  passesIf("is not empty", Collection<E>::isNotEmpty)

/**
 * Maps an assertion on a collection to an assertion on its size.
 *
 * @see Collection.size
 */
val <T : Collection<*>> Asserter<T>.size: Asserter<Int>
  get() = map(Collection<*>::size)
