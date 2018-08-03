package strikt.assertions

import strikt.api.Asserter
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * Asserts that the subject has a [CharSequence.length] of exactly [expected].
 */
fun <T : CharSequence> Asserter<T>.hasLength(expected: Int): Asserter<T> =
  assert("has length %d", expected) {
    when (subject.length) {
      expected -> pass()
      else -> fail(actual = subject.length)
    }
  }

/**
 * Asserts that the subject is composed of all lower-case characters.
 */
fun <T : CharSequence> Asserter<T>.isLowerCase(): Asserter<T> =
  passesIf("is lower case") {
    all { it.isLowerCase() }
  }

/**
 * Asserts that the subject is composed of all upper-case characters.
 */
fun <T : CharSequence> Asserter<T>.isUpperCase(): Asserter<T> =
  passesIf("is upper case") {
    all { it.isUpperCase() }
  }

/**
 * Asserts that the subject starts with the [expected] character.
 */
fun <T : CharSequence> Asserter<T>.startsWith(expected: Char): Asserter<T> =
  passesIf("starts with %s", expected) {
    startsWith(expected)
  }

/**
 * Asserts that the subject starts with the [expected] string.
 */
fun <T : CharSequence> Asserter<T>.startsWith(expected: CharSequence): Asserter<T> =
  passesIf("starts with %s", expected) {
    startsWith(expected)
  }

/**
 * Asserts that the subject is a full match for the [expected] regular
 * expression.
 */
fun <T : CharSequence> Asserter<T>.matches(expected: Regex): Asserter<T> =
  passesIf("matches the regular expression %s", expected) {
    matches(expected)
  }

/**
 * Asserts that the subject is a full match for the [expected] regular
 * expression regardless of case.
 */
fun <T : CharSequence> Asserter<T>.matchesIgnoringCase(expected: Regex): Asserter<T> =
  passesIf("matches the regular expression %s (ignoring case)", expected) {
    Regex(expected.pattern, IGNORE_CASE).let {
      matches(it)
    }
  }

/**
 * Asserts that the subject contains a match for the [expected] regular
 * expression.
 */
fun <T : CharSequence> Asserter<T>.contains(expected: Regex): Asserter<T> =
  passesIf("contains a match for the regular expression %s", expected) {
    contains(expected)
  }

/**
 * Asserts that the subject contains a match for the [expected] regular
 * expression regardless of case.
 */
fun <T : CharSequence> Asserter<T>.containsIgnoringCase(expected: Regex): Asserter<T> =
  passesIf("contains a match for the regular expression %s (ignoring case)", expected) {
    Regex(expected.pattern, IGNORE_CASE).let {
      contains(it)
    }
  }

/**
 * Asserts that the subject contains the [expected] substring.
 */
fun <T : CharSequence> Asserter<T>.contains(expected: CharSequence): Asserter<T> =
  passesIf("contains %s", expected) {
    contains(expected)
  }

/**
 * Asserts that the subject contains the [expected] substring regardless of
 * case.
 */
fun <T : CharSequence> Asserter<T>.containsIgnoringCase(expected: CharSequence): Asserter<T> =
  passesIf("contains %s (ignoring case)", expected) {
    contains(expected, ignoreCase = true)
  }

/**
 * Maps an assertion on a [CharSequence] to an assertion on its length.
 *
 * @see CharSequence.length
 */
val <T : CharSequence> Asserter<T>.length: Asserter<Int>
  get() = map(CharSequence::length)
