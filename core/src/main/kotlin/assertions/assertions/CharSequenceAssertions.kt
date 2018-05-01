package assertions.assertions

import assertions.api.Assertion

/**
 * Asserts that the subject has a [CharSequence.length] of exactly [expected].
 */
fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  apply {
    atomic("has length $expected") { subject ->
      if (subject.length == expected) {
        success()
      } else {
        failure()
      }
    }
  }

/**
 * Asserts that the subject is composed of all lower-case characters.
 */
fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  apply {
    atomic("is lower case") { subject ->
      if (subject.all { it.isLowerCase() }) {
        success()
      } else {
        failure()
      }
    }
  }

/**
 * Asserts that the subject is composed of all upper-case characters.
 */
fun <T : CharSequence> Assertion<T>.isUpperCase(): Assertion<T> =
  apply {
    atomic("is upper case") { subject ->
      if (subject.all { it.isUpperCase() }) {
        success()
      } else {
        failure()
      }
    }
  }

/**
 * Asserts that the subject starts with the [expected] character.
 */
fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  apply {
    atomic("starts with '$expected'") { subject ->
      if (subject.startsWith(expected)) {
        success()
      } else {
        failure()
      }
    }
  }
