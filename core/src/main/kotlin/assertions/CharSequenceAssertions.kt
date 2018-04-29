package assertions

fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.length == expected) {
        Success
      } else {
        Failure(target, "Expected length of $target to be $expected but was ${target.length}")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.all { it.isLowerCase() }) {
        Success
      } else {
        Failure(target, "Expected $target to be lower case")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.startsWith(expected)) {
        Success
      } else {
        Failure(target, "Expected $target to start with $expected")
      }
    }
  }
