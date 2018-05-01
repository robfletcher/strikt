package assertions

interface Assertion<T> {
  fun evaluate(description: String, predicate: AssertionContext.(T) -> Unit)
}

interface AssertionContext {
  fun success()
  fun failure()

  // TODO: should be a sub-interface for nested assertions only (different `evaluate`)?
  fun <T> expect(subject: T): Assertion<T>

  fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T>
  val anyFailed: Boolean
  val allFailed: Boolean
  val anySucceeded: Boolean
  val allSucceeded: Boolean
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

      override fun <T> expect(subject: T): Assertion<T> {
        return ReportingAssertion(aggregatingReporter, subject)
      }

      override fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T> {
        return ReportingAssertion(aggregatingReporter, subject)
          .apply(block)
      }

      override val anyFailed: Boolean
        get() = aggregatingReporter.anyFailed

      override val allFailed: Boolean
        get() = aggregatingReporter.allFailed

      override val anySucceeded: Boolean
        get() = aggregatingReporter.anySucceeded

      override val allSucceeded: Boolean
        get() = aggregatingReporter.allSucceeded
    }
      .predicate(subject)
  }
}
