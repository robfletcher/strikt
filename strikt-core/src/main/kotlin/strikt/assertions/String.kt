package strikt.assertions

import strikt.api.Assertion

/**
 * Asserts that the subject is equal to the expected value regardless of case.
 */
fun Assertion<String>.isEqualToIgnoringCase(expected: String): Assertion<String> =
  assert("is equal to \"$expected\" (ignoring case)") {
    when {
      subject.equals(expected, ignoreCase = true) -> pass()
      else                                        -> fail(expected, subject)
    }
  }
