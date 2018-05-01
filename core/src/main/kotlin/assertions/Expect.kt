package assertions

fun <T> expect(subject: T): Assertion<T> = FailFastAssertion(subject)

fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T> =
  CollectingAssertion(subject)
    .apply(block)
    .apply {
      if (results.any { it.status == Status.Failure }) {
        throw AssertionFailed(results)
      }
    }
