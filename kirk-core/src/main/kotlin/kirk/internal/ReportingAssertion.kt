package kirk.internal

import kirk.api.*

internal class ReportingAssertion<T>(
  private val reporter: Reporter,
  private val subject: T
) : Assertion<T> {
  override fun assert(description: String, assertion: AssertionContext<T>.() -> Unit) =
    apply {
      AssertionContextImpl(subject, reporter, description).assertion()
    }

  override fun <R> map(function: T.() -> R): Assertion<R> {
    return ReportingAssertion(reporter, subject.function())
  }
}

private class AssertionContextImpl<T>(
  override val subject: T,
  val reporter: Reporter,
  val description: String
) : AssertionContext<T> {
  override fun pass() {
    reporter.report(result(Status.Passed, description, subject))
  }

  override fun fail() {
    reporter.report(result(Status.Failed, description, subject))
  }

  override fun compose(assertions: ComposedAssertionContext.() -> Unit): ComposedAssertionResults =
    AggregatingReporter().let { nestedReporter ->
      ComposedAssertionContextImpl(nestedReporter)
        .apply(assertions)
        .let {
          ComposedAssertionResultsImpl(reporter, nestedReporter, description, subject)
        }
    }
}

private class ComposedAssertionContextImpl(private val nestedReporter: AggregatingReporter) : ComposedAssertionContext {
  override fun <T> expect(subject: T): Assertion<T> {
    return ReportingAssertion(nestedReporter, subject)
  }

  override fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T> {
    return ReportingAssertion(nestedReporter, subject)
      .apply(block)
  }
}

private class ComposedAssertionResultsImpl(
  private val reporter: Reporter,
  private val nestedReporter: AggregatingReporter,
  private val description: String,
  private val subject: Any?
) : ComposedAssertionResults {
  override fun pass() {
    reporter.report(result(Status.Passed, description, subject, nestedReporter.results))
  }

  override fun fail() {
    reporter.report(result(Status.Failed, description, subject, nestedReporter.results))
  }

  override val anyFailed = nestedReporter.anyFailed
  override val allFailed = nestedReporter.allFailed
  override val anyPassed = nestedReporter.anyPassed
  override val allPassed = nestedReporter.allPassed
}
