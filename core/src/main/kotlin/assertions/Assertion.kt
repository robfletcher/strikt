package assertions

class Assertion<T : Any> constructor(val target: T?)

fun <T : Any> Assertion<T>.isNull(): Assertion<Nothing> =
  if (target == null) {
    Assertion(target)
  } else {
    throw AssertionError("Expected $target to be null")
  }

fun <T : Any> Assertion<T>.isNotNull(): Assertion<T> =
  if (target == null) {
    throw AssertionError("Expected $target not to be null")
  } else {
    this
  }

inline fun <reified ST : Any> Assertion<*>.isA(): Assertion<ST> =
  when (target) {
    null  -> throw AssertionError("Expected $target to be an instance of ${ST::class.java.name} but it is null")
    is ST -> @Suppress("UNCHECKED_CAST") Assertion(target as ST)
    else  -> throw AssertionError("Expected $target to be an instance of ${ST::class.java.name} but it is a ${target.javaClass.name}")
  }

