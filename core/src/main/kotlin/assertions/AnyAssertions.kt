package assertions

class Assertion<T> internal constructor(private val target: T?) {
  fun isNull(): Assertion<Nothing> {
    if (target == null) {
      return Assertion(target)
    } else {
      throw AssertionError("Expected $target to be null")
    }
  }

  fun isNotNull(): Assertion<T> {
    if (target == null) {
      throw AssertionError("Expected $target not to be null")
    } else {
      return this
    }
  }
}
