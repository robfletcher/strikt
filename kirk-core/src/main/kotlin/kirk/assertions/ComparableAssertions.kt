package kirk.assertions

import kirk.api.Assertion

/**
 * Asserts that the subject compares greater than [expected] according to
 * Kotlin's standard `>` operator.
 */
fun <T : Comparable<T>> Assertion<T>.isGreaterThan(expected: T): Assertion<T> =
  atomic("is greater than $expected") {
    when {
      subject > expected -> pass()
      else               -> fail()
    }
  }

/**
 * Asserts that the subject compares less than [expected] according to Kotlin's
 * standard `<` operator.
 */
fun <T : Comparable<T>> Assertion<T>.isLessThan(expected: T): Assertion<T> =
  atomic("is less than $expected") {
    when {
      subject < expected -> pass()
      else               -> fail()
    }
  }

/**
 * Asserts that the subject compares greater than or equal to [expected]
 * according to Kotlin's standard `>=` operator.
 */
fun <T : Comparable<T>> Assertion<T>.isGreaterThanOrEqualTo(expected: T): Assertion<T> =
  atomic("is greater than or equal to $expected") {
    when {
      subject >= expected -> pass()
      else                -> fail()
    }
  }

/**
 * Asserts that the subject compares less than or equal to [expected] according
 * to Kotlin's standard `<=` operator.
 */
fun <T : Comparable<T>> Assertion<T>.isLessThanOrEqualTo(expected: T): Assertion<T> =
  atomic("is less than or equal to $expected") {
    when {
      subject <= expected -> pass()
      else                -> fail()
    }
  }