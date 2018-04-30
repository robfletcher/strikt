package assertions

fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  apply {
    evaluate("has size $expected") { subject ->
      if (subject.size == expected) {
        success(subject)
      } else {
        failure(subject)
      }
    }
  }

fun <T : Iterable<E>, E> Assertion<T>.allMatch(predicate: Assertion<E>.() -> Unit) =
  apply {
    evaluate("all elements match predicate") { subject ->
      val results = mutableListOf<Result>()
      subject.forEach { element ->
        val compoundAssertion = CollectingAssertion(element)
        compoundAssertion.predicate()
        results.addAll(compoundAssertion.results)
      }

      if (results.all { it is Success }) {
        success(subject, results)
      } else {
        failure(subject, results)
      }
    }
  }
