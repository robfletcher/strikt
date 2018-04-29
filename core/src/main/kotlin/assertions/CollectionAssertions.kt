package assertions

fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  apply {
    evaluate { target ->
      if (expected == target.size) {
        Success(target, "has size $expected")
      } else {
        Failure(target, "has size $expected but is ${target.size}")
      }
    }
  }

fun <T : Iterable<E>, E> Assertion<T>.allMatch(predicate: Assertion<E>.() -> Unit) =
  apply {
    evaluate { target ->
      target.all {
        // TODO: this should report mismatches not blow up on first one
        FailFastAssertion(it).predicate()
        true
      }
      Success(target, "all elements match")
    }
  }
