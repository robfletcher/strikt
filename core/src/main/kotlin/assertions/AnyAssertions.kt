package assertions

fun <T> Assertion<T?>.isNull(): Assertion<Nothing> {
  evaluate { target ->
    when (target) {
      null -> AtomicSuccess(target, "is null")
      else -> AtomicFailure(target, "is null")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<Nothing>
}

fun <T> Assertion<T?>.isNotNull(): Assertion<T> {
  evaluate { target ->
    if (target == null) {
      AtomicFailure(target, "is not null")
    } else {
      AtomicSuccess(target, "is not null")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}

inline fun <reified T> Assertion<*>.isA(): Assertion<T> {
  evaluate { target ->
    when (target) {
      null -> AtomicFailure(target, "is an instance of ${T::class.java.name} but it is null")
      is T -> AtomicSuccess(target, "is an instance of ${T::class.java.name}")
      else -> AtomicFailure(target, "is an instance of ${T::class.java.name} but it is a ${target.javaClass.name}")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}
