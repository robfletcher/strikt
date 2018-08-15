package strikt.api

import kotlin.jvm.internal.CallableReference

/**
 * Allows assertion implementations to determine a result.
 */
interface Assertion {
  /**
   * Mark this result as passed.
   */
  fun pass()

  /**
   * Mark this result as failed.
   *
   * @param actual an optional actual value, that is the value that differed
   * from the expectation.
   * @param description A description of the failure. May contain a
   * [String.format] style placeholder for the [actual] value.
   * @property cause The exception that caused the failure, if any.
   */
  fun fail(
    actual: Any? = null,
    description: String? = if (actual == null) null else "found %s",
    cause: Throwable? = null
  )

  /**
   * Used to construct assertions.
   *
   * @see expect
   * @see Assertion
   */
  interface Builder<T> {

    /**
     * Evaluates a condition that may pass or fail.
     *
     * While this method _can_ be used directly in a test but is typically used
     * inside an extension method on `Assertion<T>` such as those provided in the
     * [strikt.assertions] package.
     *
     * @param description a description for the condition the assertion evaluates.
     * @param assert the assertion implementation that should result in a call
     * to [Assertion.pass] or [Assertion.fail].
     * @return this assertion, in order to facilitate a fluent API.
     * @see Assertion.pass
     * @see Assertion.fail
     */
    fun assert(
      description: String,
      assert: AtomicAssertion.(T) -> Unit
    ): Builder<T> =
      assert(description, null, assert)

    /**
     * Evaluates a condition that may pass or fail.
     *
     * While this method _can_ be used directly in a test but is typically used
     * inside an extension method on `Assertion<T>` such as those provided in the
     * [strikt.assertions] package.
     *
     * @param description a description for the condition the assertion evaluates.
     * @param expected the expected value of a comparison.
     * @param assert the assertion implementation that should result in a call
     * to [Assertion.pass] or [Assertion.fail].
     * @return this assertion, in order to facilitate a fluent API.
     * @see Assertion.pass
     * @see Assertion.fail
     */
    fun assert(
      description: String,
      expected: Any?,
      assert: AtomicAssertion.(T) -> Unit
    ): Builder<T>

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
      assertions: Builder<T>.(T) -> Unit
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
      assertions: Builder<T>.(T) -> Unit
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
    fun passesIf(description: String, assert: T.() -> Boolean): Builder<T> =
      apply {
        assert(description) {
          if (it.assert()) pass() else fail()
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
    ): Builder<T> =
      apply {
        assert(description, expected) {
          if (it.assert()) pass() else fail()
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
     * @param function a lambda whose receiver is the current assertion subject.
     * @return an assertion whose subject is the value returned by [function].
     */
    // TODO: not sure about this name, it's fundamentally similar to Kotlin's run. Also it might be nice to have a dedicated `map` for Assertion<Iterable>.
    fun <R> map(function: (T) -> R): DescribeableBuilder<R> =
      when (function) {
        is CallableReference -> map(".${function.propertyName} %s", function)
        else -> map("%s", function)
      }

    /**
     * Maps the assertion subject to the result of [function].
     * This is useful for chaining to property values or method call results on
     * the subject.
     *
     * @param description a description of the mapped result.
     * @param function a lambda whose receiver is the current assertion subject.
     * @return an assertion whose subject is the value returned by [function].
     */
    fun <R> map(description: String, function: (T) -> R): DescribeableBuilder<R>

    /**
     * Reverses any assertions chained after this method.
     *
     * @return an assertion that negates the results of any assertions applied to
     * its subject.
     */
    fun not(): Builder<T>

    private val CallableReference.propertyName: String
      get() = "^get(.+)$".toRegex().find(name).let { match ->
        return when (match) {
          null -> name
          else -> match.groupValues[1].decapitalize()
        }
      }
  }
}
