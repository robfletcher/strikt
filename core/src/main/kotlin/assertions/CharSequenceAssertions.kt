package assertions

fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  if (expected == target.length) this
  else throw AssertionError("Expected length of $target to be $expected but was ${target.length}")