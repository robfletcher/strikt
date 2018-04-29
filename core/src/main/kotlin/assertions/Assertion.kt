package assertions

class Assertion<T> constructor(val target: T)

fun <T> Assertion<T>.isNull(): Assertion<Nothing> =
  if (target == null) {
    this as Assertion<Nothing>
  } else {
    throw AssertionError("Expected $target to be null")
  }

fun <T> Assertion<T>.isNotNull(): Assertion<T> =
  if (target == null) {
    throw AssertionError("Expected $target not to be null")
  } else {
    this
  }

inline fun <reified T : Any> Assertion<*>.isA(): Assertion<T> =
  when (target) {
    null -> throw AssertionError("Expected $target to be an instance of ${T::class.java.name} but it is null")
    is T -> @Suppress("UNCHECKED_CAST") Assertion(target as T)
    else -> throw AssertionError("Expected $target to be an instance of ${T::class.java.name} but it is a ${target.javaClass.name}")
  }

