package assertions

fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  when {
    target == null            -> throw AssertionError("Expected length of $target to be $expected but it was null")
    target.length == expected -> this
    else                      -> throw AssertionError("Expected length of $target to be $expected but was ${target.length}")
  }