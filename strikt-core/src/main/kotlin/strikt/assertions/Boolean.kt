package strikt.assertions

import strikt.api.Assertion

/**
 * Asserts that the subject is not null and is the boolean value `true`.
 */
fun <T : Boolean?> Assertion<T>.isTrue() =
  assert("is true") {
    if (subject == true) pass() else fail(actual = subject)
  }

/**
 * Asserts that the subject is not null and is the boolean value `false`.
 */
fun <T : Boolean?> Assertion<T>.isFalse() =
  assert("is true") {
    if (subject == false) pass() else fail(actual = subject)
  }
