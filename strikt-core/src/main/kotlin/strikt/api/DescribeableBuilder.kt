package strikt.api

import strikt.api.Assertion.Builder

/**
 * Extension of [Assertion.Builder] that enables the description of the
 * assertion subject.
 *
 * Since it doesn't make sense to do this anywhere except directly after the
 * initial [expectThat] or [Assertion.Builder.get] call those methods return an
 * instance of this interface, while assertions themselves just return
 * [Assertion.Builder].
 */
interface DescribeableBuilder<T> : Builder<T> {
  /**
   * Adds a description to the assertion.
   *
   * @param description a description of the subject of the assertion. The
   * description may include a [String.format] style placeholder for the value
   * itself.
   * @return the same assertion with the new description applied.
   */
  fun describedAs(description: String): Builder<T>

  /**
   * Adds a description to the assertion.
   *
   * @param descriptor a function that returns a description for the subject of
   * the assertion. The subject is the function's `this` so it's possible to use
   * properties or method results of the subject in the description.
   * @return the same assertion with the new description applied.
   */
  fun describedAs(descriptor: T.() -> String): Builder<T>
}
