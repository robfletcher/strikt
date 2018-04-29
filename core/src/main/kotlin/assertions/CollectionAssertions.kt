package assertions

fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  if (expected == target.size) {
    this
  } else {
    throw AssertionError("Expected size of $target to be $expected but was ${target.size}")
  }

fun <T : Iterable<E>, E> Assertion<T>.allMatch(predicate: Assertion<E>.() -> Unit) =
  target.forEach {
    Assertion(it).predicate()
  }