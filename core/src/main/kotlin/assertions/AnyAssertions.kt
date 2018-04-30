package assertions

fun <T> Assertion<T?>.isNull(): Assertion<Nothing> {
  evaluate { subject ->
    when (subject) {
      null -> Result.success(subject, "is null")
      else -> Result.failure(subject, "is null")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<Nothing>
}

fun <T> Assertion<T?>.isNotNull(): Assertion<T> {
  evaluate { subject ->
    if (subject == null) {
      Result.failure(subject, "is not null")
    } else {
      Result.success(subject, "is not null")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}

inline fun <reified T> Assertion<*>.isA(): Assertion<T> {
  evaluate { subject ->
    when (subject) {
      null -> Result.failure(subject, "is an instance of ${T::class.java.name} but it is null")
      is T -> Result.success(subject, "is an instance of ${T::class.java.name}")
      else -> Result.failure(subject, "is an instance of ${T::class.java.name} but it is a ${subject.javaClass.name}")
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}
