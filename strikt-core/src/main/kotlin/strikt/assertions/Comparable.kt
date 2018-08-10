package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Asserts that the subject compares greater than [expected] according to
 * Kotlin's standard `>` operator.
 */
fun <T : Comparable<T>> Builder<T>.isGreaterThan(expected: T): Builder<T> =
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
fun <T : Comparable<T>> Builder<T>.isLessThan(expected: T): Builder<T> =
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
fun <T : Comparable<T>> Builder<T>.isGreaterThanOrEqualTo(expected: T): Builder<T> =
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
fun <T : Comparable<T>> Builder<T>.isLessThanOrEqualTo(expected: T): Builder<T> =
  assert("is less than or equal to %s", expected) {
    when {
      subject <= expected -> pass()
      else -> fail()
    }
  }
