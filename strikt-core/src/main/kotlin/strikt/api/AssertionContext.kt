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
   * Report that the assertion failed optionally providing additional detail.
   *
   * @param actual the value(s) that violated the assertion.
   * @param message a message describing the failure.
   * @param cause an underlying exception that was the cause of the failure.
   */
  fun fail(actual: Any? = null, message: String? = null, cause: Throwable? = null) =
    fail(Failure(actual, message, cause))

  fun fail(failure: Failure)

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