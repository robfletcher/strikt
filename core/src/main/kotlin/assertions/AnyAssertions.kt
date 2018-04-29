package assertions

fun <T> Assertion<T?>.isNull(): Assertion<Nothing> {
  evaluate { target ->
    when (target) {
      null -> Result.success(target, "is null")
      else -> Result.failure(target, "is null")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<Nothing>
}

fun <T> Assertion<T?>.isNotNull(): Assertion<T> {
  evaluate { target ->
    if (target == null) {
      Result.failure(target, "is not null")
    } else {
      Result.success(target, "is not null")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}

inline fun <reified T> Assertion<*>.isA(): Assertion<T> {
  evaluate { target ->
    when (target) {
      null -> Result.failure(target, "is an instance of ${T::class.java.name} but it is null")
      is T -> Result.success(target, "is an instance of ${T::class.java.name}")
      else -> Result.failure(target, "is an instance of ${T::class.java.name} but it is a ${target.javaClass.name}")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}
