package strikt.assertions

import strikt.api.Asserter

/**
 * Asserts that the subject is not null and is the boolean value `true`.
 */
fun <T : Boolean?> Asserter<T>.isTrue() =
  assert("is true", expected = true) {
    if (subject == true) pass() else fail(actual = subject)
  }

/**
 * Asserts that the subject is not null and is the boolean value `false`.
 */
fun <T : Boolean?> Asserter<T>.isFalse() =
  assert("is false", expected = false) {
    if (subject == false) pass() else fail(actual = subject)
  }
