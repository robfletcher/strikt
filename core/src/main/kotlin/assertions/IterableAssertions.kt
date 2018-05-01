package assertions

fun <T : Iterable<E>, E> Assertion<T>.all(predicate: Assertion<E>.() -> Unit) =
  apply {
    evaluate("all elements match predicate") { subject ->
      // TODO: this could be an expect method to simplify similar implementations
      aggregating { reporter ->
        subject.forEach {
          ReportingAssertion(reporter, it).apply(predicate)
        }
        if (reporter.results.all { it.status == Status.Success }) {
          success()
        } else {
          failure()
        }
      }
    }
  }