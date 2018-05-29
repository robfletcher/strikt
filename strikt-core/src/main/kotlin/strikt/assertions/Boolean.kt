package strikt.assertions

import strikt.api.Assertion

fun Assertion<Boolean>.isTrue() =
  passesIf("is true") { this }

fun Assertion<Boolean>.isFalse() =
  passesIf("is false") { !this }