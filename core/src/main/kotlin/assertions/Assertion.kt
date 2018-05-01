package assertions

interface Assertion<T> {
  fun evaluate(description: String, predicate: AssertionContext.(T) -> Unit)
  fun evaluateNested(description: String, predicate: NestedAssertionContext.(T) -> Unit)
}

interface AssertionContext {
  fun success()
  fun failure()
}

interface NestedAssertionContext : AssertionContext {
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
    AssertionContextImpl(reporter, description, subject).predicate(subject)
  }

  override fun evaluateNested(description: String, predicate: NestedAssertionContext.(T) -> Unit) {
    NestedAssertionContextImpl(reporter, description, subject).predicate(subject)
  }
}

private class AssertionContextImpl<T>(val reporter: Reporter, val description: String, val subject: T) : AssertionContext {
  override fun success() {
    reporter.report(result(Status.Success, description, subject))
  }

  override fun failure() {
    reporter.report(result(Status.Failure, description, subject))
  }
}

private class NestedAssertionContextImpl<T>(val reporter: Reporter, val description: String, val subject: T) : NestedAssertionContext {
  private val aggregatingReporter = AggregatingReporter()

  override fun success() {
    reporter.report(result(Status.Success, description, subject, aggregatingReporter.results))
  }

  override fun failure() {
    reporter.report(result(Status.Failure, description, subject, aggregatingReporter.results))
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
