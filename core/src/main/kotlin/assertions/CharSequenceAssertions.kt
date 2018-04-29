package assertions

fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.length == expected) {
        AtomicSuccess(target, "has length $expected")
      } else {
        AtomicFailure(target, "has length $expected but is ${target.length}")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.all { it.isLowerCase() }) {
        AtomicSuccess(target, "is lower case")
      } else {
        AtomicFailure(target, "is lower case")
      }
    }
  }

fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.startsWith(expected)) {
        AtomicSuccess(target, "starts with '$expected'")
      } else {
        AtomicFailure(target, "starts with '$expected'")
      }
    }
  }
