package assertions

fun <T> expect(target: T): Assertion<T> = FailFastAssertion(target)

fun <T> expect(target: T, block: Assertion<T>.() -> Unit): Assertion<T> =
  CollectingAssertion(target)
    .apply(block)
    .apply {
      if (results.any { it is Failure }) {
        throw AssertionFailed(results)
      }
    }
