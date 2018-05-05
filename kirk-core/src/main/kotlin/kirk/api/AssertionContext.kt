package kirk.api

import kirk.internal.AggregatingReporter
import kirk.internal.Reporter
import kirk.internal.result

/**
 * Allows reporting of success or failure by assertion implementations.
 *
 * This class is the receiver of the lambda passed to [Assertion.assert].
 *
 * @see Assertion.assert
 */
class AssertionContext<T>
internal constructor(
  /**
   * The assertion subject.
   */
  val subject: T,
  private val reporter: Reporter,
  private val description: String
) {
  /**
   * Report that the assertion succeeded.
   */
  fun pass() {
    reporter.report(result(Status.Passed, description, subject))
  }

  /**
   * Report that the assertion failed.
   */
  fun fail() {
    reporter.report(result(Status.Failed, description, subject))
  }

  /**
   * Allows an assertion to be composed of multiple sub-assertions such as on
   * fields of an object or elements of a collection.
   *
   * The results of assertions made inside the [assertions] block are included
   * under the overall assertion result.
   *
   * @return the results of assertions made inside the [assertions] block.
   */
  fun compose(assertions: ComposedAssertions.() -> Unit): ComposedAssertionResults =
    AggregatingReporter().let { nestedReporter ->
      ComposedAssertions(nestedReporter)
        .apply(assertions)
        .let {
          ComposedAssertionResults(reporter, nestedReporter, description, subject)
        }
    }
}
