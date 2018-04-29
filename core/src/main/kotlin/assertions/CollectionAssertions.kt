package assertions

fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  apply {
    evaluate { target ->
      if (target.size == expected) {
        AtomicSuccess(target, "has size $expected")
      } else {
        AtomicFailure(target, "has size $expected but is ${target.size}")
      }
    }
  }

fun <T : Iterable<E>, E> Assertion<T>.allMatch(block: Assertion<E>.() -> Unit) =
  apply {
    evaluate { target ->
      val results = mutableListOf<Result>()
      target.forEach { element ->
        val compoundAssertion = object : Assertion<E> {
          override fun evaluate(predicate: (E) -> Result) {
            predicate(element).let(results::add)
          }
        }
        compoundAssertion.block()
      }

      if (results.all { it is AtomicSuccess<*> }) {
        CompoundSuccess(target, results)
      } else {
        CompoundFailure(target, results)
      }
    }
  }
