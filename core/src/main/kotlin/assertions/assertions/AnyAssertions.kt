package assertions.assertions

import assertions.api.Assertion

fun <T> Assertion<T?>.isNull(): Assertion<Nothing> {
  atomic("is null") { subject ->
    when (subject) {
      null -> success()
      else -> failure()
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<Nothing>
}

fun <T> Assertion<T?>.isNotNull(): Assertion<T> {
  atomic("is not null") { subject ->
    when (subject) {
      null -> failure()
      else -> success()
    }
  }
  @Suppress("UNCHECKED_CAST")
  return this as Assertion<T>
}

inline fun <reified T> Assertion<*>.isA(): Assertion<T> {
  atomic("is an instance of ${T::class.java.name}") { subject ->
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
    atomic("is equal to $expected") { subject ->
      when (subject) {
        expected -> success()
        else     -> failure()
      }
    }
  }
