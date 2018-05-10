package kirk.api

import kirk.internal.AssertionResultCollector

/**
 * Allows assertions to be composed, or nested, using
 * [AssertionContext.compose].
 */
class ComposedAssertions
internal constructor(private val nestedReporter: AssertionResultCollector) {
  /**
   * Start a chain of assertions in the current nested context.
   *
   * @param subject the subject of the chain of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @return an assertion for [subject].
   */
  fun <T> expect(subject: T): Assertion<T> {
    return Assertion(nestedReporter, subject)
  }

  /**
   * Evaluate a block of assertions in the current nested context.
   *
   * @param subject the subject of the block of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   * @return an assertion for [subject].
   */
  fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T> {
    return Assertion(nestedReporter, subject).apply(block)
  }

  /**
   * Sometimes you just need to fail with a description.
   * This method lets you do that.
   *
   * @param subject the subject of the failure.
   * @param description a description for the failure.
   */
  fun <T> fail(subject: T, description: String) {
    nestedReporter.report(Result(Status.Failed, description, subject))
  }
}
