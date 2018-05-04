package kirk.internal

import kirk.api.Assertion
import kirk.api.AssertionContext

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
  private val nestedReporter = AggregatingReporter()

  override fun pass() {
    if (nestedReporter.results.isEmpty()) {
      reporter.report(result(Status.Success, description, subject))
    } else {
      reporter.report(result(Status.Success, description, subject, nestedReporter.results))
    }
  }

  override fun fail() {
    if (nestedReporter.results.isEmpty()) {
      reporter.report(result(Status.Failure, description, subject))
    } else {
      reporter.report(result(Status.Failure, description, subject, nestedReporter.results))
    }
  }

  override fun <T> expect(subject: T): Assertion<T> {
    return ReportingAssertion(nestedReporter, subject)
  }

  override fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T> {
    return ReportingAssertion(nestedReporter, subject)
      .apply(block)
  }

  override val anyFailed: Boolean
    get() = nestedReporter.anyFailed

  override val allFailed: Boolean
    get() = nestedReporter.allFailed

  override val anyPassed: Boolean
    get() = nestedReporter.anyPassed

  override val allPassed: Boolean
    get() = nestedReporter.allPassed
}