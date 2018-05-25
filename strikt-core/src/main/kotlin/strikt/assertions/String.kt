package strikt.assertions

import strikt.api.Assertion

/**
 * Asserts that the subject is equal to the expected value regardless of case.
 */
fun Assertion<String>.isEqualToIgnoringCase(expected: String): Assertion<String> =
  assert("is equal to %s (ignoring case)", expected) {
    when {
      subject.equals(expected, ignoreCase = true) -> pass()
      else -> fail()
    }
  }
