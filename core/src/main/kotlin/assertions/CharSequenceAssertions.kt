package assertions

fun <T : CharSequence> Assertion<T>.hasLength(expected: Int): Assertion<T> =
  if (expected == target.length) this
  else throw AssertionError("Expected length of $target to be $expected but was ${target.length}")

fun <T : CharSequence> Assertion<T>.isLowerCase(): Assertion<T> =
  if (target.all { it.isLowerCase() }) this
  else throw AssertionError("Expected $target to be lower case")

fun <T : CharSequence> Assertion<T>.startsWith(expected: Char): Assertion<T> =
  if (target.startsWith(expected)) this
  else throw AssertionError("Expected $target to start with $expected")