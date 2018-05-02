package kirk.assertions

import kirk.api.Assertion

/**
 * Asserts that the subject has a [CharSequence.length] of exactly [expected].
 */
fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  atomic("has length $expected") {
    when (subject.length) {
      expected -> pass()
      else     -> fail()
    }
  }

/**
 * Asserts that the subject is composed of all lower-case characters.
 */
fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  atomic("is lower case") {
    when {
      subject.all { it.isLowerCase() } -> pass()
      else                             -> fail()
    }
  }

/**
 * Asserts that the subject is composed of all upper-case characters.
 */
fun <T : CharSequence> Assertion<T>.isUpperCase(): Assertion<T> =
  atomic("is upper case") {
    when {
      subject.all { it.isUpperCase() } -> pass()
      else                             -> fail()
    }
  }

/**
 * Asserts that the subject starts with the [expected] character.
 */
fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  atomic("starts with '$expected'") {
    when {
      subject.startsWith(expected) -> pass()
      else                         -> fail()
    }
  }

/**
 * Asserts that the subject is a full match for the [expected] regular
 * expression.
 */
fun <T : CharSequence> Assertion<T>.matches(expected: Regex): Assertion<T> =
  atomic("matches the regular expression /${expected.pattern}/") {
    when {
      subject.matches(expected) -> pass()
      else                      -> fail()
    }
  }