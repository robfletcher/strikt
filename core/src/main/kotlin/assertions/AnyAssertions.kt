package assertions

import kotlin.reflect.KClass

open class Assertion<T : Any> internal constructor(protected val target: T?) {
  fun isNull(): Assertion<Nothing> =
    if (target == null) {
      Assertion(target)
    } else {
      throw AssertionError("Expected $target to be null")
    }

  fun isNotNull(): Assertion<T> =
    if (target == null) {
      throw AssertionError("Expected $target not to be null")
    } else {
      this
    }

  fun <ST : T> isA(type: KClass<ST>): Assertion<ST> =
    when {
      target == null          -> throw AssertionError("Expected $target to be an instance of $type but it is null")
      type.isInstance(target) -> Assertion(target as ST)
      else                    -> throw AssertionError("Expected $target to be an instance of $type but it is a ${target.javaClass.name}")
    }

  inline fun <reified ST : T> isA(): Assertion<ST> = isA(ST::class)

  fun isString(): CharSequenceAssertion<String> =
    when (target) {
      null      -> throw AssertionError("Expected $target to be an instance of String but it is null")
      is String -> CharSequenceAssertion(target)
      else      -> throw AssertionError("Expected $target to be an instance of String but it is a ${target.javaClass.name}")
    }
}

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