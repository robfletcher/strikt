package kirk.api

import kirk.api.Status.Failed
import kirk.api.Status.Passed
import kirk.internal.AssertionResultCollector
import kirk.internal.AssertionResultHandler
import kirk.internal.Described

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
  private val resultHandler: AssertionResultHandler,
  private val nestedReporter: AssertionResultCollector,
  private val assertionDescription: String,
  private val subjectDescription: String,
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
    resultHandler.report(
      Result(
        Passed,
        assertionDescription,
        Described(subjectDescription, subject),
        nestedResults = nestedReporter.results
      )
    )
  }

  /**
   * Report that the assertion failed.
   */
  fun fail() {
    resultHandler.report(
      Result(
        Failed,
        assertionDescription,
        Described(subjectDescription, subject),
        nestedResults = nestedReporter.results
      )
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
    resultHandler.report(Result(
      Failed,
      assertionDescription,
      Described(subjectDescription, subject),
      Described(actualDescription, actualValue),
      nestedResults = nestedReporter.results
    ))
  }

  val anyFailed: Boolean = nestedReporter.anyFailed
  val allFailed: Boolean = nestedReporter.allFailed
  val anyPassed: Boolean = nestedReporter.anyPassed
  val allPassed: Boolean = nestedReporter.allPassed
}
