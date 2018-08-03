package strikt.assertions

import strikt.api.Asserter

/**
 * Asserts that the subject compares greater than [expected] according to
 * Kotlin's standard `>` operator.
 */
fun <T : Comparable<T>> Asserter<T>.isGreaterThan(expected: T): Asserter<T> =
  assert("is greater than %s", expected) {
    when {
      subject > expected -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the subject compares less than [expected] according to Kotlin's
 * standard `<` operator.
 */
fun <T : Comparable<T>> Asserter<T>.isLessThan(expected: T): Asserter<T> =
  assert("is less than %s", expected) {
    when {
      subject < expected -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the subject compares greater than or equal to [expected]
 * according to Kotlin's standard `>=` operator.
 */
fun <T : Comparable<T>> Asserter<T>.isGreaterThanOrEqualTo(expected: T): Asserter<T> =
  assert("is greater than or equal to %s", expected) {
    when {
      subject >= expected -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the subject compares less than or equal to [expected] according
 * to Kotlin's standard `<=` operator.
 */
fun <T : Comparable<T>> Asserter<T>.isLessThanOrEqualTo(expected: T): Asserter<T> =
  assert("is less than or equal to %s", expected) {
    when {
      subject <= expected -> pass()
      else -> fail()
    }
  }
