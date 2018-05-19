package strikt.api

/**
 * The receiver of the lambda passed to [Assertion.assert].
 * This class allows evaluation of the [subject] value, or of composed
 * assertions, ultimately resulting in a call to [pass] or [fail].
 */
interface AssertionContext<T> {
  /**
   * The value that is the subject of the assertion.
   */
  val subject: T

  /**
   * Report that the assertion succeeded.
   */
  fun pass()

  /**
   * Report that the assertion failed.
   */
  fun fail()

  /**
   * Report that the assertion failed providing detail about the actual value
   * of a comparison that caused the assertion to fail.
   *
   * @param actualDescription descriptive text about [actualValue] including a
   * placeholder in [String.format] notation for [actualValue].
   * @param actualValue theR value(s) that violated the assertion.
   */
  fun fail(actual: Any?)

  /**
   * Allows an assertion to be composed of multiple sub-assertions such as on
   * fields of an object or elements of a collection.
   *
   * The results of assertions made inside the [assertions] block are included
   * under the overall assertion result.
   *
   * @return the results of assertions made inside the [assertions] block.
   */
  fun compose(assertions: ComposedAssertions<T>.() -> Unit): ComposedAssertionContext
}