package kirk.api

import kirk.internal.AssertionResultCollector
import kirk.internal.AssertionResultHandler

/**
 * The results of assertions made inside the block passed to
 * [AssertionContext.compose].
 *
 * @property anyFailed `true` if any nested assertions evaluated inside
 * [AssertionContext.compose] failed, `false` otherwise.
 * @property allFailed `true` if _all_ nested assertions evaluated using
 * [AssertionContext.compose] failed, `false` otherwise.
 * @property anyPassed `true` if any nested assertions evaluated using
 * [AssertionContext.compose] passed, `false` otherwise.
 * @property allPassed `true` if _all_ nested assertions evaluated using
 * [AssertionContext.compose] passed, `false` otherwise.
 */
class ComposedAssertionResults
internal constructor(
  private val assertionResultHandler: AssertionResultHandler,
  private val nestedReporter: AssertionResultCollector,
  private val description: String,
  private val subject: Any?
) {
  /**
   * A convenient way to chain a result handler after
   * [AssertionContext.compose].
   */
  infix fun results(block: ComposedAssertionResults.() -> Unit) = apply(block)

  /**
   * Report that the assertion succeeded.
   */
  fun pass() {
    assertionResultHandler.report(
      Result(Status.Passed, description, subject, nestedResults = nestedReporter.results)
    )
  }

  /**
   * Report that the assertion failed.
   */
  fun fail() {
    assertionResultHandler.report(
      Result(Status.Failed, description, subject, nestedResults = nestedReporter.results)
    )
  }

  /**
   * Report that the assertion failed.
   *
   * @param actualDescription descriptive text about [actualValue] including a
   * placeholder in [String.format] notation for [actualValue].
   * @param actualValue the value(s) that violated the assertion.
   */
  fun fail(actualDescription: String, actualValue: Any?) {
    assertionResultHandler.report(Result(
      Status.Failed,
      description,
      subject,
      Actual(actualDescription, actualValue),
      nestedResults = nestedReporter.results
    ))
  }

  val anyFailed: Boolean = nestedReporter.anyFailed
  val allFailed: Boolean = nestedReporter.allFailed
  val anyPassed: Boolean = nestedReporter.anyPassed
  val allPassed: Boolean = nestedReporter.allPassed
}
