package strikt.assertions

import strikt.api.Assertion

/**
 * Asserts that the subject is `null`.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Assertion<T?>.isNull(): Assertion<Nothing> =
  assert("is null", null) {
    when (subject) {
      null -> pass()
      else -> fail()
    }
  } as Assertion<Nothing>

/**
 * Asserts that the subject is not `null`.
 *
 * @return an assertion for a non-`null` subject.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Assertion<T?>.isNotNull(): Assertion<T> =
  assert("is not null") {
    when (subject) {
      null -> fail()
      else -> pass()
    }
  } as Assertion<T>

/**
 * Asserts that the subject is an instance of [T].
 *
 * @return an assertion for [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Assertion<*>.isA(): Assertion<T> =
  assert("is an instance of %s", T::class.java) {
    when (subject) {
      null -> fail(actual = null)
      is T -> pass()
      else -> fail(actual = subject?.javaClass)
    }
  } as Assertion<T>

/**
 * Asserts that the subject is equal to [expected] according to the standard
 * Kotlin `==` operator.
 *
 * @param expected the expected value.
 */
fun <T> Assertion<T>.isEqualTo(expected: Any?): Assertion<T> =
  assert("is equal to %s", expected) {
    when (subject) {
      expected -> pass()
      else -> fail(actual = subject)
    }
  }

/**
 * Asserts that the subject is not equal to [expected] according to the standard
 * Kotlin `==` operator.
 *
 * @param expected the expected value.
 */
fun <T> Assertion<T>.isNotEqualTo(expected: Any?): Assertion<T> =
  assert("is not equal to %s", expected) {
    when (subject) {
      expected -> fail()
      else -> pass()
    }
  }

/**
 * Asserts that the subject is the same instance as [expected] according to the standard
 * Kotlin `===` operator.
 *
 * @param expected the expected instance.
 */
fun <T> Assertion<T>.isSameInstanceAs(expected: Any?): Assertion<T> =
  assert("is the same instance as %s", expected) {
    when {
      subject === expected -> pass()
      else -> fail(actual = subject)
    }
  }

/**
 * Asserts that the subject is not the same instance as [expected] according to the standard
 * Kotlin `===` operator.
 *
 * @param expected the expected instance.
 */
fun <T> Assertion<T>.isNotSameInstanceAs(expected: Any?): Assertion<T> =
  assert("is not the same instance as %s", expected) {
    when {
      subject === expected -> fail()
      else -> pass()
    }
  }
