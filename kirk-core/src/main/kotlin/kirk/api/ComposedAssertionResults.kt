package kirk.api

import kirk.internal.AggregatingReporter
import kirk.internal.Reporter
import kirk.internal.result

/**
 * The results of assertions made inside the block passed to
 * [AssertionContext.compose].
 */
class ComposedAssertionResults
internal constructor(
  private val reporter: Reporter,
  private val nestedReporter: AggregatingReporter,
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
    reporter.report(result(Status.Passed, description, subject, nestedReporter.results))
  }

  /**
   * Report that the assertion failed.
   */
  fun fail() {
    reporter.report(result(Status.Failed, description, subject, nestedReporter.results))
  }

  /**
   * Returns `true` if any nested assertions evaluated inside
   * [AssertionContext.compose] failed, `false` otherwise.
   *
   * @see expect
   */
  val anyFailed: Boolean = nestedReporter.anyFailed

  /**
   * Returns `true` if _all_ nested assertions evaluated using
   * [AssertionContext.compose] failed, `false` otherwise.
   *
   * @see expect
   */
  val allFailed: Boolean = nestedReporter.allFailed

  /**
   * Returns `true` if any nested assertions evaluated using
   * [AssertionContext.compose] passed, `false` otherwise.
   *
   * @see expect
   */
  val anyPassed: Boolean = nestedReporter.anyPassed

  /**
   * Returns `true` if _all_ nested assertions evaluated using
   * [AssertionContext.compose] passed, `false` otherwise.
   *
   * @see expect
   */
  val allPassed: Boolean = nestedReporter.allPassed
}
