package assertions.assertions

import assertions.api.Assertion

/**
 * Asserts that the subject has a [Collection.size] of exactly [expected].
 */
fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  atomic("has size $expected") { subject ->
    when (subject.size) {
      expected -> success()
      else     -> failure()
    }
  }
