package strikt.assertions

import strikt.api.Assertion
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * Asserts that the subject has a [CharSequence.length] of exactly [expected].
 */
fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  assert("has length $expected") {
    when (subject.length) {
      expected -> pass()
      else     -> fail("found %d", subject.length)
    }
  }

/**
 * Asserts that the subject is composed of all lower-case characters.
 */
fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  passesIf("is lower case") {
    all { it.isLowerCase() }
  }

/**
 * Asserts that the subject is composed of all upper-case characters.
 */
fun <T : CharSequence> Assertion<T>.isUpperCase(): Assertion<T> =
  passesIf("is upper case") {
    all { it.isUpperCase() }
  }

/**
 * Asserts that the subject starts with the [expected] character.
 */
fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  passesIf("starts with '$expected'") {
    startsWith(expected)
  }

/**
 * Asserts that the subject is a full match for the [expected] regular
 * expression.
 */
fun <T : CharSequence> Assertion<T>.matches(expected: Regex): Assertion<T> =
  passesIf("matches the regular expression /${expected.pattern}/") {
    matches(expected)
  }

/**
 * Asserts that the subject is a full match for the [expected] regular
 * expression regardless of case.
 */
fun <T : CharSequence> Assertion<T>.matchesIgnoringCase(expected: Regex): Assertion<T> =
  passesIf("matches the regular expression /${expected.pattern}/ (ignoring case)") {
    Regex(expected.pattern, IGNORE_CASE).let {
      matches(it)
    }
  }

/**
 * Asserts that the subject contains a match for the [expected] regular
 * expression.
 */
fun <T : CharSequence> Assertion<T>.contains(expected: Regex): Assertion<T> =
  passesIf("contains a match for the regular expression /${expected.pattern}/") {
    contains(expected)
  }

/**
 * Asserts that the subject contains a match for the [expected] regular
 * expression regardless of case.
 */
fun <T : CharSequence> Assertion<T>.containsIgnoringCase(expected: Regex): Assertion<T> =
  passesIf("contains a match for the regular expression /${expected.pattern}/ (ignoring case)") {
    Regex(expected.pattern, IGNORE_CASE).let {
      contains(it)
    }
  }

/**
 * Asserts that the subject contains the [expected] substring.
 */
fun <T : CharSequence> Assertion<T>.contains(expected: CharSequence): Assertion<T> =
  passesIf("contains \"$expected\"") {
    contains(expected)
  }

/**
 * Asserts that the subject contains the [expected] substring regardless of
 * case.
 */
fun <T : CharSequence> Assertion<T>.containsIgnoringCase(expected: CharSequence): Assertion<T> =
  passesIf("contains \"$expected\" (ignoring case)") {
    contains(expected, ignoreCase = true)
  }

/**
 * Maps an assertion on a [CharSequence] to an assertion on its length.
 */
val <T : CharSequence> Assertion<T>.length: Assertion<Int>
  get() = map(CharSequence::length)