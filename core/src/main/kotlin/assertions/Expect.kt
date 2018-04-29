package assertions

fun <T> expect(target: T, block: Assertion<T>.() -> Unit) {
  Assertion(target).apply(block)
}

fun <T> expect(target: T): Assertion<T> = Assertion(target)
