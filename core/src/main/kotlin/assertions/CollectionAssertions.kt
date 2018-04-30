package assertions

fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  apply {
    evaluate { subject ->
      if (subject.size == expected) {
        Result.success(subject, "has size $expected")
      } else {
        Result.failure(subject, "has size $expected")
      }
    }
  }

fun <T : Iterable<E>, E> Assertion<T>.allMatch(predicate: Assertion<E>.() -> Unit) =
  apply {
    evaluate { subject ->
      val results = mutableListOf<Result>()
      subject.forEach { element ->
        val compoundAssertion = CollectingAssertion(element)
        compoundAssertion.predicate()
        results.addAll(compoundAssertion.results)
      }

      if (results.all { it is Success }) {
        Result.success(subject, "all elements match predicate", results)
      } else {
        Result.failure(subject, "all elements match predicate", results)
      }
    }
  }
