package assertions

fun <T : Any> expect(target: T?, block: Assertion<T>.() -> Unit) {
  Assertion(target).apply(block)
}

fun <T : Any> expect(target: T?): Assertion<T> = Assertion(target)
