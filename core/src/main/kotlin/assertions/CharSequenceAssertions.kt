package assertions

fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.length == expected) {
        Success(target, "has length $expected")
      } else {
        Failure(target, "has length $expected but is ${target.length}")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.all { it.isLowerCase() }) {
        Success(target, "is lower case")
      } else {
        Failure(target, "is lower case")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.startsWith(expected)) {
        Success(target, "starts with '$expected'")
      } else {
        Failure(target, "starts with '$expected'")
      }
    }
  }
