package assertions

class Assertion<T : Any> internal constructor(private val target: T?) {
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

  fun <ST : T> isA(type: Class<ST>): Assertion<ST> {
    when {
      target == null                          -> throw AssertionError("Expected $target to be an instance of $type but it is null")
      type.isAssignableFrom(target.javaClass) -> return Assertion(target as ST)
      else                                    -> throw AssertionError("Expected $target to be an instance of $type but it is a ${target.javaClass.name}")
    }
  }

  inline fun <reified ST : T> isA(): Assertion<ST> = isA(ST::class.java)
}
