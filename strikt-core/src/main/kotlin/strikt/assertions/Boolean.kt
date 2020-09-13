package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Asserts that the subject is not null and is the boolean value `true`.
 */
fun <T : Boolean?> Builder<T>.isTrue() =
  assert("is true", expected = true) {
    if (it == true) pass(actual = it) else fail(actual = it)
  }

/**
 * Asserts that the subject is not null and is the boolean value `false`.
 */
fun <T : Boolean?> Builder<T>.isFalse() =
  assert("is false", expected = false) {
    if (it == false) pass(actual = it) else fail(actual = it)
  }
