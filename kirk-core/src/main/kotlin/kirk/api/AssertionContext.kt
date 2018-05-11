package kirk.api

import kirk.api.Status.Failed
import kirk.api.Status.Passed
import kirk.internal.AssertionResultCollector
import kirk.internal.AssertionResultHandler
import kirk.internal.Described

/**
 * Allows reporting of success or failure by assertion implementations.
 *
 * This class is the receiver of the lambda passed to [Assertion.assert].
 *
 * @property subject The assertion subject.
 * @see Assertion.assert
 */
class AssertionContext<T>
internal constructor(
  private val subjectDescription: String,
  val subject: T,
  private val assertionResultHandler: AssertionResultHandler,
  private val description: String
) {
  /**
   * Report that the assertion succeeded.
   */
  fun pass() {
    assertionResultHandler.report(
      Result(Passed, description, Described(subject))
    )
  }

  /**
   * Report that the assertion failed.
   */
  fun fail() {
    assertionResultHandler.report(
      Result(Failed, description, Described(subject))
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
    assertionResultHandler.report(
      Result(
        Failed,
        description,
        Described(subjectDescription, subject),
        Described(actualDescription, actualValue)
      )
    )
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
  fun compose(assertions: ComposedAssertions<T>.() -> Unit): ComposedAssertionResults =
    AssertionResultCollector().let { nestedReporter ->
      ComposedAssertions(nestedReporter, subject)
        .apply(assertions)
        .let {
          ComposedAssertionResults(
            assertionResultHandler,
            nestedReporter,
            description,
            subjectDescription,
            subject
          )
        }
    }
}
