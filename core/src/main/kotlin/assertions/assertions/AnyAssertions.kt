package assertions.assertions

import assertions.api.Assertion

/**
 * Asserts that the subject is `null`.
 */
fun <T> Assertion<T?>.isNull(): Assertion<Nothing> {
  atomic("is null") { subject ->
    when (subject) {
      null -> success()
      else -> failure()
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<Nothing>
}

/**
 * Asserts that the subject is not `null`.
 *
 * @return an assertion for a non-`null` subject.
 */
fun <T> Assertion<T?>.isNotNull(): Assertion<T> {
  atomic("is not null") { subject ->
    when (subject) {
      null -> failure()
      else -> success()
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}

/**
 * Asserts that the subject is an instance of [T].
 *
 * @return an assertion for [T].
 */
inline fun <reified T> Assertion<*>.isA(): Assertion<T> {
  atomic("is an instance of ${T::class.java.name}") { subject ->
    when (subject) {
      null -> failure()
      is T -> success()
      else -> failure()
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}

/**
 * Asserts that the subject is equal to [expected] according to the standard
 * Kotlin `==` operator.
 *
 * @param expected the expected value.
 */
fun <T> Assertion<T>.isEqualTo(expected: Any?): Assertion<T> =
  apply {
    atomic("is equal to $expected") { subject ->
      when (subject) {
        expected -> success()
        else     -> failure()
      }
    }
  }

/**
 * Asserts that the subject is not equal to [expected] according to the standard
 * Kotlin `==` operator.
 *
 * @param expected the expected value.
 */
fun <T> Assertion<T>.isNotEqualTo(expected: Any?): Assertion<T> =
  apply {
    atomic("is equal to $expected") { subject ->
      when (subject) {
        expected -> failure()
        else     -> success()
      }
    }
  }
