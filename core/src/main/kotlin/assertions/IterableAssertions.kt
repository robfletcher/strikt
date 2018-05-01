package assertions

fun <T : Iterable<E>, E> Assertion<T>.all(predicate: Assertion<E>.() -> Unit) =
  apply {
    evaluate("all elements match predicate") { subject ->
      subject.forEach {
        expect(it, predicate)
      }
      if (allSucceeded) {
        success()
      } else {
        failure()
      }
    }
  }