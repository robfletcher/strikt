package assertions

class CharSequenceAssertion<T : CharSequence>
internal constructor(target: T?)
  : Assertion<T>(target) {
  fun hasLength(expected: Int): CharSequenceAssertion<T> =
    when {
      target == null            -> throw AssertionError("Expected length of $target to be $expected but it was null")
      target.length == expected -> this
      else                      -> throw AssertionError("Expected length of $target to be $expected but was ${target.length}")
    }
}