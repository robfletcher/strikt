package kirk.api

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
   * Report that the assertion failed.
   *
   * @param actualDescription descriptive text about [actualValue] including a
   * placeholder in [String.format] notation for [actualValue].
   * @param actualValue theR value(s) that violated the assertion.
   */
  fun fail(actualDescription: String, actualValue: Any?)

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