package assertions.assertions

import assertions.api.Assertion

fun <T> Assertion<T?>.isNull(): Assertion<Nothing> {
  evaluate("is null") { subject ->
    when (subject) {
      null -> success()
      else -> failure()
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<Nothing>
}

fun <T> Assertion<T?>.isNotNull(): Assertion<T> {
  evaluate("is not null") { subject ->
    when (subject) {
      null -> failure()
      else -> success()
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}

inline fun <reified T> Assertion<*>.isA(): Assertion<T> {
  evaluate("is an instance of ${T::class.java.name}") { subject ->
    when (subject) {
      null -> failure()
      is T -> success()
      else -> failure()
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}

fun <T> Assertion<T>.isEqualTo(expected: Any?): Assertion<T> =
  apply {
    evaluate("is equal to $expected") { subject ->
      when (subject) {
        expected -> success()
        else     -> failure()
      }
    }
  }
