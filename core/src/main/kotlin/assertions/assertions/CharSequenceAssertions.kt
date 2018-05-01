package assertions.assertions

import assertions.api.Assertion

fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  apply {
    atomic("has length $expected") { subject ->
      if (subject.length == expected) {
        success()
      } else {
        failure()
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  apply {
    atomic("is lower case") { subject ->
      if (subject.all { it.isLowerCase() }) {
        success()
      } else {
        failure()
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isUpperCase(): Assertion<T> =
  apply {
    atomic("is upper case") { subject ->
      if (subject.all { it.isUpperCase() }) {
        success()
      } else {
        failure()
      }
    }
  }

fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  apply {
    atomic("starts with '$expected'") { subject ->
      if (subject.startsWith(expected)) {
        success()
      } else {
        failure()
      }
    }
  }
