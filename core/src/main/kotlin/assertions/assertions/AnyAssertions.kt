package assertions.assertions

import assertions.api.Assertion

/**
 * Asserts that the subject is `null`.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Assertion<T?>.isNull(): Assertion<Nothing> =
  atomic("is null") {
    when (subject) {
      null -> success()
      else -> failure()
    }
  } as Assertion<Nothing>

/**
 * Asserts that the subject is not `null`.
 *
 * @return an assertion for a non-`null` subject.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Assertion<T?>.isNotNull(): Assertion<T> =
  atomic("is not null") {
    when (subject) {
      null -> failure()
      else -> success()
    }
  } as Assertion<T>

/**
 * Asserts that the subject is an instance of [T].
 *
 * @return an assertion for [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Assertion<*>.isA(): Assertion<T> =
  atomic("is an instance of ${T::class.java.name}") {
    when (subject) {
      null -> failure()
      is T -> success()
      else -> failure()
    }
  } as Assertion<T>

/**
 * Asserts that the subject is equal to [expected] according to the standard
 * Kotlin `==` operator.
 *
 * @param expected the expected value.
 */
fun <T> Assertion<T>.isEqualTo(expected: Any?): Assertion<T> =
  atomic("is equal to $expected") {
    when (subject) {
      expected -> success()
      else     -> failure()
    }
  }

/**
 * Asserts that the subject is not equal to [expected] according to the standard
 * Kotlin `==` operator.
 *
 * @param expected the expected value.
 */
fun <T> Assertion<T>.isNotEqualTo(expected: Any?): Assertion<T> =
  atomic("is equal to $expected") {
    when (subject) {
      expected -> failure()
      else     -> success()
    }
  }
