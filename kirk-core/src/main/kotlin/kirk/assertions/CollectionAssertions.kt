package kirk.assertions

import kirk.api.Assertion

/**
 * Asserts that the subject has a [Collection.size] of exactly [expected].
 */
fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  atomic("has size $expected") {
    when (subject.size) {
      expected -> pass()
      else     -> fail()
    }
  }
