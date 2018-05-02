package assertions.internal

import assertions.api.Assertion
import assertions.api.AtomicAssertionContext
import assertions.api.NestedAssertionContext

internal class ReportingAssertion<T>(
  private val reporter: Reporter,
  private val subject: T
) : Assertion<T> {
  override fun atomic(description: String, assertion: AtomicAssertionContext<T>.() -> Unit) =
    apply {
      AtomicAssertionContextImpl(subject, reporter, description).assertion()
    }

  override fun nested(description: String, assertions: NestedAssertionContext<T>.() -> Unit) =
    apply {
      NestedAssertionContextImpl(subject, reporter, description).assertions()
    }

  override fun <R> map(function: T.() -> R): Assertion<R> {
    return ReportingAssertion(reporter, subject.function())
  }
}

private class AtomicAssertionContextImpl<T>(
  override val subject: T,
  val reporter: Reporter,
  val description: String
) : AtomicAssertionContext<T> {
  override fun pass() {
    reporter.report(result(Status.Success, description, subject))
  }

  override fun fail() {
    reporter.report(result(Status.Failure, description, subject))
  }
}

private class NestedAssertionContextImpl<T>(
  override val subject: T,
  val reporter: Reporter,
  val description: String
) : NestedAssertionContext<T> {
  private val nestedReporter = AggregatingReporter()

  override fun pass() {
    reporter.report(result(Status.Success, description, subject, nestedReporter.results))
  }

  override fun fail() {
    reporter.report(result(Status.Failure, description, subject, nestedReporter.results))
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

  override val anySucceeded: Boolean
    get() = nestedReporter.anySucceeded

  override val allSucceeded: Boolean
    get() = nestedReporter.allSucceeded
}