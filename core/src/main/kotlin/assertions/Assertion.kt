package assertions

interface Assertion<T> {
  fun evaluate(description: String, predicate: AssertionContext.(T) -> Unit)
}

interface AssertionContext {
  fun success()
  fun failure()
  fun aggregating(block: AssertionContext.(AggregatingReporter) -> Unit)
}

internal class ReportingAssertion<T>(
  private val reporter: Reporter,
  private val subject: T
) : Assertion<T> {
  override fun evaluate(description: String, predicate: AssertionContext.(T) -> Unit) {
    object : AssertionContext {

      val aggregatingReporter = AggregatingReporter()

      override fun success() {
        if (aggregatingReporter.results.isEmpty()) {
          reporter.report(result(Status.Success, description, subject))
        } else {
          reporter.report(result(Status.Success, description, subject, aggregatingReporter.results))
        }
      }

      override fun failure() {
        if (aggregatingReporter.results.isEmpty()) {
          reporter.report(result(Status.Failure, description, subject))
        } else {
          reporter.report(result(Status.Failure, description, subject, aggregatingReporter.results))
        }
      }

      override fun aggregating(block: AssertionContext.(AggregatingReporter) -> Unit) {
        block(aggregatingReporter)
      }
    }
      .predicate(subject)
  }
}
