package strikt.assertions

import strikt.api.Assertion.Builder
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * Asserts that the subject has a [CharSequence.length] of exactly [expected].
 */
fun <T : CharSequence> Builder<T>.hasLength(expected: Int): Builder<T> =
  assert("has length %d", expected) {
    when (it.length) {
      expected -> pass()
      else -> fail(expected = expected, actual = it.length)
    }
  }

/**
 * Asserts that the subject is composed of all lower-case characters.
 */
fun <T : CharSequence> Builder<T>.isLowerCase(): Builder<T> =
  passesIf("is lower case") {
    it.all { it.isLowerCase() }
  }

/**
 * Asserts that the subject is composed of all upper-case characters.
 */
fun <T : CharSequence> Builder<T>.isUpperCase(): Builder<T> =
  passesIf("is upper case") {
    it.all { it.isUpperCase() }
  }

/**
 * Asserts that the subject starts with the [expected] character.
 */
fun <T : CharSequence> Builder<T>.startsWith(expected: Char): Builder<T> =
  passesIf("starts with %s", expected) {
    it.startsWith(expected)
  }

/**
 * Asserts that the subject starts with the [expected] string.
 */
fun <T : CharSequence> Builder<T>.startsWith(expected: CharSequence): Builder<T> =
  passesIf("starts with %s", expected) {
    it.startsWith(expected)
  }

/**
 * Asserts that the subject is a full match for the [expected] regular
 * expression.
 */
fun <T : CharSequence> Builder<T>.matches(expected: Regex): Builder<T> =
  passesIf("matches the regular expression %s", expected) {
    it.matches(expected)
  }

/**
 * Asserts that the subject is a full match for the [expected] regular
 * expression regardless of case.
 */
fun <T : CharSequence> Builder<T>.matchesIgnoringCase(expected: Regex): Builder<T> =
  passesIf("matches the regular expression %s (ignoring case)", expected) { subject ->
    Regex(expected.pattern, IGNORE_CASE).let {
      subject.matches(it)
    }
  }

/**
 * Asserts that the subject contains a match for the [expected] regular
 * expression.
 */
fun <T : CharSequence> Builder<T>.contains(expected: Regex): Builder<T> =
  passesIf("contains a match for the regular expression %s", expected) {
    it.contains(expected)
  }

/**
 * Asserts that the subject contains a match for the [expected] regular
 * expression regardless of case.
 */
fun <T : CharSequence> Builder<T>.containsIgnoringCase(expected: Regex): Builder<T> =
  passesIf("contains a match for the regular expression %s (ignoring case)", expected) { subject ->
    Regex(expected.pattern, IGNORE_CASE).let {
      subject.contains(it)
    }
  }

/**
 * Asserts that the subject contains the [expected] substring.
 */
fun <T : CharSequence> Builder<T>.contains(expected: CharSequence): Builder<T> =
  passesIf("contains %s", expected) {
    it.contains(expected)
  }

/**
 * Asserts that the subject contains the [expected] substring regardless of
 * case.
 */
fun <T : CharSequence> Builder<T>.containsIgnoringCase(expected: CharSequence): Builder<T> =
  passesIf("contains %s (ignoring case)", expected) {
    it.contains(expected, ignoreCase = true)
  }

/**
 * Maps an assertion on a [CharSequence] to an assertion on its length.
 *
 * @see CharSequence.length
 */
val <T : CharSequence> Builder<T>.length: Builder<Int>
  get() = map(CharSequence::length)
