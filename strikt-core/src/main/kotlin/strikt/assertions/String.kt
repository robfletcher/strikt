package strikt.assertions

import strikt.api.Asserter

/**
 * Asserts that the subject is equal to the expected value regardless of case.
 */
fun Asserter<String>.isEqualToIgnoringCase(expected: String): Asserter<String> =
  assert("is equal to %s (ignoring case)", expected) {
    when {
      subject.equals(expected, ignoreCase = true) -> pass()
      else -> fail()
    }
  }
