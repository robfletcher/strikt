package kirk.api

/**
 * Allows reporting of success or failure by assertion implementations.
 *
 * This class is the receiver of the lambda passed to [Assertion.assert].
 *
 * @see Assertion.assert
 */
interface AssertionContext<T> {
  /**
   * The assertion subject.
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
   * Allows an assertion to be composed of multiple sub-assertions such as on
   * fields of an object or elements of a collection.
   *
   * The results of assertions made inside the [assertions] block are included
   * under the overall assertion result.
   *
   * @return the results of assertions made inside the [assertions] block.
   */
  fun compose(assertions: ComposedAssertionContext.() -> Unit): ComposedAssertionResults
}
