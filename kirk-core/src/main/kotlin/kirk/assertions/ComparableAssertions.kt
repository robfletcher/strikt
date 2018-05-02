package kirk.assertions

import kirk.api.Assertion

fun <T : Comparable<T>> Assertion<T>.isGreaterThan(expected: T): Assertion<T> =
  atomic("is greater than $expected") {
    when {
      subject > expected -> pass()
      else               -> fail()
    }
  }

fun <T : Comparable<T>> Assertion<T>.isLessThan(expected: T): Assertion<T> =
  atomic("is greater than $expected") {
    when {
      subject < expected -> pass()
      else               -> fail()
    }
  }