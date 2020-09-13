package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Asserts that the subject is equal to the expected value regardless of case.
 */
infix fun Builder<String>.isEqualToIgnoringCase(expected: String): Builder<String> =
  assert("is equal to %s (ignoring case)", expected) {
    when {
      it.equals(expected, ignoreCase = true) -> pass(actual = it)
      else -> fail(actual = it)
    }
  }
