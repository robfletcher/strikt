package strikt.api

import kotlin.jvm.internal.CallableReference

/**
 * Holds a subject of type [T] that you can then make assertions about.
 */
interface Asserter<T> {
  /**
   * Adds a description to the assertion.
   *
   * @param description a description of the subject of the assertion. The
   * description may include a [String.format] style placeholder for the value
   * itself.
   * @return the same assertion with the new description applied.
   */
  // TODO: doesn't really make sense to expose this method in a way that it can be called any time other than right after expect, create a new interface that extends this one and adds this one method
  fun describedAs(description: String): Asserter<T>

  /**
   * Evaluates multiple assertions against the subject.
   *
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   *
   * @see expect
   */
  // TODO: not 100% happy with the name, or the need for this method
  fun assertAll(block: Asserter<T>.() -> Unit): Asserter<T>

  /**
   * Evaluates a condition that may pass or fail.
   *
   * While this method _can_ be used directly in a test but is typically used
   * inside an extension method on `Assertion<T>` such as those provided in the
   * [strikt.assertions] package.
   *
   * @sample strikt.samples.AssertionMethods.assert
   *
   * @param description a description for the condition the assertion evaluates.
   * @param assert the assertion implementation that should result in a call
   * to [AtomicAssertion.pass] or [AtomicAssertion.fail].
   * @return this assertion, in order to facilitate a fluent API.
   * @see AtomicAssertion.pass
   * @see AtomicAssertion.fail
   */
  fun assert(
    description: String,
    assert: AtomicAssertion<T>.() -> Unit
  ): Asserter<T> =
    assert(description, null, assert)

  /**
   * Evaluates a condition that may pass or fail.
   *
   * While this method _can_ be used directly in a test but is typically used
   * inside an extension method on `Assertion<T>` such as those provided in the
   * [strikt.assertions] package.
   *
   * @sample strikt.samples.AssertionMethods.assert
   *
   * @param description a description for the condition the assertion evaluates.
   * @param expected the expected value of a comparison.
   * @param assert the assertion implementation that should result in a call
   * to [AtomicAssertion.pass] or [AtomicAssertion.fail].
   * @return this assertion, in order to facilitate a fluent API.
   * @see AtomicAssertion.pass
   * @see AtomicAssertion.fail
   */
  fun assert(
    description: String,
    expected: Any?,
    assert: AtomicAssertion<T>.() -> Unit
  ): Asserter<T>

  /**
   * Allows an assertion to be composed of multiple sub-assertions such as on
   * fields of an object or elements of a collection.
   *
   * The results of assertions made inside the [assertions] block are included
   * under the overall assertion result.
   *
   * @param description a description for the condition the assertion evaluates.
   * @param expected the expected value of a comparison.
   * @param assertions a group of assertions that will be evaluated against the
   * subject.
   * @return the results of assertions made inside the [assertions] block used
   * to assertAll whether the overall assertion passes or fails.
   */
  fun compose(
    description: String,
    expected: Any?,
    assertions: AssertionComposer<T>.() -> Unit
  ): CompoundAssertions<T>

  /**
   * Allows an assertion to be composed of multiple sub-assertions such as on
   * fields of an object or elements of a collection.
   *
   * The results of assertions made inside the [assertions] block are included
   * under the overall assertion result.
   *
   * @param description a description for the condition the assertion evaluates.
   * @param assertions a group of assertions that will be evaluated against the
   * subject.
   * @return the results of assertions made inside the [assertions] block used
   * to assertAll whether the overall assertion passes or fails.
   */
  fun compose(
    description: String,
    assertions: AssertionComposer<T>.() -> Unit
  ): CompoundAssertions<T> =
    compose(description, null, assertions)

  /**
   * Evaluates a boolean condition.
   * This is useful for implementing the simplest types of assertion function.
   *
   * @param description a description for the condition the assertion evaluates.
   * @param assert a function that returns `true` (the assertion passes) or
   * `false` (the assertion fails).
   * @return this assertion, in order to facilitate a fluent API.
   */
  // TODO: this name sucks
  fun passesIf(description: String, assert: T.() -> Boolean): Asserter<T> =
    apply {
      assert(description) {
        if (subject.assert()) pass() else fail()
      }
    }

  /**
   * Evaluates a boolean condition.
   * This is useful for implementing the simplest types of assertion function.
   *
   * @param description a description for the condition the assertion evaluates.
   * @param expected the expected value of a comparison.
   * @param assert a function that returns `true` (the assertion passes) or
   * `false` (the assertion fails).
   * @return this assertion, in order to facilitate a fluent API.
   */
  fun passesIf(
    description: String,
    expected: Any?,
    assert: T.() -> Boolean
  ): Asserter<T> =
    apply {
      assert(description, expected) {
        if (subject.assert()) pass() else fail()
      }
    }

  /**
   * Maps the assertion subject to the result of [function].
   * This is useful for chaining to property values or method call results on
   * the subject.
   *
   * If [function] is a callable reference, (for example a getter or property
   * reference) the subject description will be automatically determined for the
   * returned assertion.
   *
   * @sample strikt.samples.AssertionMethods.map
   *
   * @param function a lambda whose receiver is the current assertion subject.
   * @return an assertion whose subject is the value returned by [function].
   */
  // TODO: not sure about this name, it's fundamentally similar to Kotlin's run. Also it might be nice to have a dedicated `map` for Assertion<Iterable>.
  fun <R> map(function: T.() -> R): Asserter<R> =
    when (function) {
      is CallableReference -> map(".${function.propertyName} %s", function)
      else -> map("%s", function)
    }

  /**
   * Maps the assertion subject to the result of [function].
   * This is useful for chaining to property values or method call results on
   * the subject.
   *
   * @sample strikt.samples.AssertionMethods.map
   *
   * @param description a description of the mapped result.
   * @param function a lambda whose receiver is the current assertion subject.
   * @return an assertion whose subject is the value returned by [function].
   */
  fun <R> map(description: String, function: T.() -> R): Asserter<R>

  /**
   * Reverses any assertions chained after this method.
   *
   * @sample strikt.samples.AssertionMethods.not
   *
   * @return an assertion that negates the results of any assertions applied to
   * its subject.
   */
  fun not(): Asserter<T>

  private val CallableReference.propertyName: String
    get() = "^get(.+)$".toRegex().find(name).let { match ->
      return when (match) {
        null -> name
        else -> match.groupValues[1].decapitalize()
      }
    }
}
