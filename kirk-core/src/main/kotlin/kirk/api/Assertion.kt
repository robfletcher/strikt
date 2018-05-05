package kirk.api

import kirk.internal.Reporter

/**
 * Holds a subject of type [T] that you can then make assertions about.
 */
class Assertion<T>
internal constructor(
  private val reporter: Reporter,
  private val subject: T
) {
  /**
   * Evaluates a condition that may pass or fail.
   *
   * This method can be used directly in a test but is typically used inside an
   * extension method on `Assertion<T>` such as those provided in the
   * [kirk.assertions] package.
   *
   * @param description a description for the assertion.
   * @param assertion the assertion implementation that should result in a call
   * to [AssertionContext.pass] or [AssertionContext.fail].
   * @return this assertion, in order to facilitate a fluent API.
   * @see AssertionContext.pass
   * @see AssertionContext.fail
   */
  fun assert(description: String, assertion: AssertionContext<T>.() -> Unit) =
    apply {
      AssertionContext(subject, reporter, description).assertion()
    }

  /**
   * Maps the assertion subject to the result of [function].
   * For example, if [function] is a property reference on [T]
   */
  // TODO: not sure about this name, it's fundamentally similar to Kotlin's run. Also it might be nice to have a dedicated `map` for Assertion<Iterable>.
  fun <R> map(function: T.() -> R): Assertion<R> {
    return Assertion(reporter, subject.function())
  }
}
