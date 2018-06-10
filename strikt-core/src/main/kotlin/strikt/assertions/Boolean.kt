package strikt.assertions

import strikt.api.Assertion

/**
 * Asserts that the subject is not null and is the boolean value `true`.
 */
fun <T : Boolean?> Assertion<T>.isTrue() =
  passesIf("is true") { this == true }

/**
 * Asserts that the subject is not null and is the boolean value `false`.
 */
fun <T : Boolean?> Assertion<T>.isFalse() =
  passesIf("is false") { this == false }
