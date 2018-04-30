package assertions

fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.length == expected) {
        Result.success(target, "has length $expected")
      } else {
        Result.failure(target, "has length $expected but is ${target.length}")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.all { it.isLowerCase() }) {
        Result.success(target, "is lower case")
      } else {
        Result.failure(target, "is lower case")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isUpperCase(): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.all { it.isUpperCase() }) {
        Result.success(target, "is upper case")
      } else {
        Result.failure(target, "is upper case")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.startsWith(expected)) {
        Result.success(target, "starts with '$expected'")
      } else {
        Result.failure(target, "starts with '$expected'")
      }
    }
  }
