package assertions

fun <T> Assertion<T?>.isNull(): Assertion<Nothing> {
  evaluate { target ->
    when (target) {
      null -> Success
      else -> Failure(target, "Expected $target to be null")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<Nothing>
}

fun <T> Assertion<T?>.isNotNull(): Assertion<T> {
  evaluate { target ->
    if (target == null) {
      Failure(target, "Expected $target not to be null")
    } else {
      Success
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}

inline fun <reified T> Assertion<*>.isA(): Assertion<T> {
  evaluate { target ->
    when (target) {
      null -> Failure(target, "Expected $target to be an instance of ${T::class.java.name} but it is null")
      is T -> Success
      else -> Failure(target, "Expected $target to be an instance of ${T::class.java.name} but it is a ${target.javaClass.name}")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}
