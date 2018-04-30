package assertions

fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  apply {
    evaluate("has length $expected") { subject ->
      if (subject.length == expected) {
        success(subject)
      } else {
        failure(subject)
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  apply {
    evaluate("is lower case") { subject ->
      if (subject.all { it.isLowerCase() }) {
        success(subject)
      } else {
        failure(subject)
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isUpperCase(): Assertion<T> =
  apply {
    evaluate("is upper case") { subject ->
      if (subject.all { it.isUpperCase() }) {
        success(subject)
      } else {
        failure(subject)
      }
    }
  }

fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  apply {
    evaluate("starts with '$expected'") { subject ->
      if (subject.startsWith(expected)) {
        success(subject)
      } else {
        failure(subject)
      }
    }
  }
