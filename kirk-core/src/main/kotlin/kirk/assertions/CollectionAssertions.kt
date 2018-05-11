package kirk.assertions

import kirk.api.Assertion

/**
 * Asserts that the subject has a [Collection.size] of exactly [expected].
 */
fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  assert("%s has size $expected") {
    when (subject.size) {
      expected -> pass()
      else     -> fail("found %d", subject.size)
    }
  }

/**
 * Asserts that the subject collection is empty.
 */
fun <T : Collection<E>, E> Assertion<T>.isEmpty(): Assertion<T> =
  assert("%s is empty") {
    if (subject.isEmpty()) pass() else fail()
  }

/**
 * Asserts that the subject collection is _not_ empty.
 */
fun <T : Collection<E>, E> Assertion<T>.isNotEmpty(): Assertion<T> =
  assert("%s is empty") {
    if (subject.isNotEmpty()) pass() else fail()
  }

/**
 * Maps an assertion on a collection to an assertion on its size.
 */
val <T : Collection<*>> Assertion<T>.size: Assertion<Int>
  get() = map(Collection<*>::size)