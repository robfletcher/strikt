package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Asserts that the subject compares greater than [expected] according to
 * Kotlin's standard `>` operator.
 */
fun <T : Comparable<T>> Builder<T>.isGreaterThan(expected: T): Builder<T> =
  assert("is greater than %s", expected) {
    when {
      it > expected -> pass()
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
      it < expected -> pass()
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
      it >= expected -> pass()
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
      it <= expected -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the subject is in the [expected] range.
 */
fun <T : Comparable<T>> Builder<T>.isIn(expected: ClosedRange<T>): Builder<T> =
  assert("is in the range %s", expected) {
    when (it) {
      in expected -> pass()
      else -> fail()
    }
  }
