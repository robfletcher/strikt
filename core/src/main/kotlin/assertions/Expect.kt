package assertions

fun <T> expect(subject: T): Assertion<T> {
  val reporter = FailFastReporter()
  return ReportingAssertion(reporter, subject)
}

fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T> {
  val reporter = AggregatingReporter()
  return ReportingAssertion(reporter, subject)
    .apply(block)
    .apply {
      if (reporter.anyFailed) {
        throw AssertionFailed(reporter.results)
      }
    }
}
