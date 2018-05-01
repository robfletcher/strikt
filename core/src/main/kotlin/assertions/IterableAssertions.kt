package assertions

fun <T : Iterable<E>, E> Assertion<T>.all(predicate: Assertion<E>.() -> Unit) =
  apply {
    evaluate("all elements match predicate") { subject ->
      // TODO: change reporter to a "sub-assertion" factory that encapsulates reporting and creating the assertion so you can just do `expect(it).apply(predicate)`
      aggregating { reporter ->
        subject.forEach {
          ReportingAssertion(reporter, it).apply(predicate)
        }
        if (reporter.allSucceeded) {
          success()
        } else {
          failure()
        }
      }
    }
  }