package assertions

fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.size == expected) {
        Result.success(target, "has size $expected")
      } else {
        Result.failure(target, "has size $expected but is ${target.size}")
      }
    }
  }

fun <T : Iterable<E>, E> Assertion<T>.allMatch(predicate: Assertion<E>.() -> Unit) =
  apply {
    evaluate { target ->
      val results = mutableListOf<Result>()
      target.forEach { element ->
        val compoundAssertion = object : Assertion<E> {
          override fun evaluate(predicate: (E) -> Result) {
            predicate(element).let(results::add)
          }
        }
        compoundAssertion.predicate()
      }

      if (results.all { it is Success }) {
        Result.success(target, "all elements match predicate", results)
      } else {
        Result.failure(target, "all elements match predicate", results)
      }
    }
  }
