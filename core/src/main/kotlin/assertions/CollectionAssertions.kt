package assertions

fun <T : Collection<E>, E> Assertion<T>.hasSize(expected: Int): Assertion<T> =
  apply {
    evaluate { target ->
      if (expected == target.size) {
        Success
      } else {
        Failure(target, "Expected size of $target to be $expected but was ${target.size}")
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
      Success
    }
  }
