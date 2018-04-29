package assertions

fun <T> expect(target: T): Assertion<T> = Assertion(target)

fun <T> expect(target: T, block: Assertion<T>.() -> Unit) =
  expect(target).apply(block)
