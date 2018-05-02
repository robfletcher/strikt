package assertions.assertions

import assertions.api.Assertion

/**
 * Asserts that the subject has a [CharSequence.length] of exactly [expected].
 */
fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  atomic("has length $expected") {
    if (subject.length == expected) {
      success()
    } else {
      failure()
    }
  }

/**
 * Asserts that the subject is composed of all lower-case characters.
 */
fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  atomic("is lower case") { 
    if (subject.all { it.isLowerCase() }) {
      success()
    } else {
      failure()
    }
  }

/**
 * Asserts that the subject is composed of all upper-case characters.
 */
fun <T : CharSequence> Assertion<T>.isUpperCase(): Assertion<T> =
  atomic("is upper case") { 
    if (subject.all { it.isUpperCase() }) {
      success()
    } else {
      failure()
    }
  }

/**
 * Asserts that the subject starts with the [expected] character.
 */
fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  atomic("starts with '$expected'") { 
    if (subject.startsWith(expected)) {
      success()
    } else {
      failure()
    }
  }
