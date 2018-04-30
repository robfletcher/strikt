package assertions

fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  apply {
    evaluate { subject ->
      if (subject.length == expected) {
        Result.success(subject, "has length $expected")
      } else {
        Result.failure(subject, "has length $expected but is ${subject.length}")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  apply {
    evaluate { subject ->
      if (subject.all { it.isLowerCase() }) {
        Result.success(subject, "is lower case")
      } else {
        Result.failure(subject, "is lower case")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isUpperCase(): Assertion<T> =
  apply {
    evaluate { subject ->
      if (subject.all { it.isUpperCase() }) {
        Result.success(subject, "is upper case")
      } else {
        Result.failure(subject, "is upper case")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  apply {
    evaluate { subject ->
      if (subject.startsWith(expected)) {
        Result.success(subject, "starts with '$expected'")
      } else {
        Result.failure(subject, "starts with '$expected'")
      }
    }
  }
