package kirk.api

import kirk.internal.AssertionResultHandler
import kirk.internal.NegatedResultHandler

/**
 * Holds a subject of type [T] that you can then make assertions about.
 */
class Assertion<T>
internal constructor(
  private val assertionResultHandler: AssertionResultHandler,
  private val subject: T
) {
  /**
   * Evaluates a condition that may pass or fail.
   *
   * This method can be used directly in a test but is typically used inside an
   * extension method on `Assertion<T>` such as those provided in the
   * [kirk.assertions] package.
   *
   * @sample kirk.samples.Assertions.inlineAssert
   * @sample kirk.samples.Assertions.assertionFunctionDefinition
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
      AssertionContext(subject, assertionResultHandler, description).assertion()
    }

  /**
   * Maps the assertion subject to the result of [function].
   * This is useful for chaining to property values or method call results on
   * the subject.
   * For example:
   *
   * ```
   * val subject = Person(
   *   name = "David",
   *   birthDate = LocalDate.of(1947, 1, 8)
   * )
   * expect(subject) {
   *   map { name }
   *     .isEqualTo("David")
   *     .map { toUpperCase() }
   *     .isEqualTo("DAVID")
   *   map { birthDate }
   *     .map { year }
   *     .isEqualTo(1947)
   * }
   * ```
   *
   * It's also possible to pass property references (for Kotlin classes):
   *
   * ```
   * expect(subject) {
   *   map(Person::name).isEqualTo("David")
   * }
   * ```
   *
   * @param function a lambda whose receiver is the current assertion subject.
   * @return an assertion whose subject is the value returned by [function].
   */
  // TODO: not sure about this name, it's fundamentally similar to Kotlin's run. Also it might be nice to have a dedicated `map` for Assertion<Iterable>.
  fun <R> map(function: T.() -> R): Assertion<R> {
    return Assertion(assertionResultHandler, subject.function())
  }

  /**
   * Reverses any assertions chained after this method.
   * For example:
   *
   * ```
   * expect("covfefe").not().isNull().isUpperCase()
   * ```
   *
   * will pass.
   *
   * @return an assertion that negates the results of any assertions applied to
   * its subject.
   */
  fun not(): Assertion<T> = Assertion(NegatedResultHandler(assertionResultHandler), subject)
}
