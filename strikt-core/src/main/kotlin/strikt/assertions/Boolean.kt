package strikt.assertions

import strikt.api.Assertion

fun <T : Boolean?> Assertion<T>.isTrue() =
  passesIf("is true") { this == true }

fun <T : Boolean?> Assertion<T>.isFalse() =
  passesIf("is false") { this == false }