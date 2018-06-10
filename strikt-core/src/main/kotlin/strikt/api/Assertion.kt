package strikt.api

import strikt.api.Mode.COLLECT
import strikt.api.Mode.FAIL_FAST
import strikt.api.reporting.Result
import strikt.api.reporting.Subject
import strikt.api.reporting.writeToString
import strikt.opentest4j.throwOnFailure
import kotlin.jvm.internal.CallableReference

/**
 * Holds a subject of type [T] that you can then make assertions about.
 */
class Assertion<T>
internal constructor(
  private val subject: Subject<T>,
  private val mode: Mode,
  private val negated: Boolean = false
) {
  /**
   * Adds a description to the assertion.
   *
   * @param description a description of the subject of the assertion. The description may include a [String.format]
   * style placeholder for the value itself.
   * @return the same assertion with the new description applied.
   */
  fun describedAs(description: String): Assertion<T> =
    Assertion(subject.copy(description = description), mode, negated)

  /**
   * Evaluates multiple assertions against the subject.
   *
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   *
   * @see expect
   */
  // TODO: not 100% happy with the name
  fun evaluate(block: Assertion<T>.() -> Unit): Assertion<T> =
    Assertion(subject, COLLECT, negated)
      .apply(block)
      .also {
        if (mode == FAIL_FAST) {
          subject.throwOnFailure()
        }
      }

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
   * @param assertion the assertion implementation that should result in a call
   * to [AssertionContext.pass] or [AssertionContext.fail].
   * @return this assertion, in order to facilitate a fluent API.
   * @see AssertionContext.pass
   * @see AssertionContext.fail
   */
  fun assert(description: String, assertion: AssertionContext<T>.() -> Unit) =
    assert(description, null, assertion)

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
   * @param assertion the assertion implementation that should result in a call
   * to [AssertionContext.pass] or [AssertionContext.fail].
   * @return this assertion, in order to facilitate a fluent API.
   * @see AssertionContext.pass
   * @see AssertionContext.fail
   */
  fun assert(description: String, expected: Any?, assertion: AssertionContext<T>.() -> Unit) =
    apply {
      val result = Result(description, expected).also(subject::append)
      AssertionContextImpl(this, result).assertion()
    }

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
   * to evaluate whether the overall assertion passes or fails.
   */
  fun compose(
    description: String,
    expected: Any?,
    assertions: ComposedAssertions<T>.() -> Unit
  ): ComposedAssertionContext<T> {
    val result = Result(description, expected).also(subject::append)
    ComposedAssertions(subject, result).apply(assertions)
    return AssertionContextImpl(this, result)
  }

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
   * to evaluate whether the overall assertion passes or fails.
   */
  fun compose(description: String, assertions: ComposedAssertions<T>.() -> Unit) =
    compose(description, null, assertions)

  /**
   * Evaluates a boolean condition.
   * This is useful for implementing the simplest types of assertion function.
   *
   * @param description a description for the condition the assertion evaluates.
   * @param assertion a function that returns `true` (the assertion passes) or
   * `false` (the assertion fails).
   * @return this assertion, in order to facilitate a fluent API.
   */
  // TODO: this name sucks
  fun passesIf(description: String, assertion: T.() -> Boolean) =
    apply {
      assert(description) {
        if (subject.assertion()) pass() else fail()
      }
    }

  /**
   * Evaluates a boolean condition.
   * This is useful for implementing the simplest types of assertion function.
   *
   * @param description a description for the condition the assertion evaluates.
   * @param expected the expected value of a comparison.
   * @param assertion a function that returns `true` (the assertion passes) or
   * `false` (the assertion fails).
   * @return this assertion, in order to facilitate a fluent API.
   */
  fun passesIf(description: String, expected: Any?, assertion: T.() -> Boolean) =
    apply {
      assert(description, expected) {
        if (subject.assertion()) pass() else fail()
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
  fun <R> map(function: T.() -> R): Assertion<R> =
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
  fun <R> map(description: String, function: T.() -> R): Assertion<R> =
    Subject(subject.value.function(), description)
      .also(subject::append)
      .let { Assertion(it, mode) }

  /**
   * Reverses any assertions chained after this method.
   *
   * @sample strikt.samples.AssertionMethods.not
   *
   * @return an assertion that negates the results of any assertions applied to
   * its subject.
   */
  fun not(): Assertion<T> = Assertion(subject, mode, !negated)

  /**
   * @return a verbose assertion report detailing all passes and failures.
   */
  fun writeReport(): String = subject.writeToString()

  private val CallableReference.propertyName: String
    get() = "^get(.+)$".toRegex().find(name).let { match ->
      return when (match) {
        null -> name
        else -> match.groupValues[1].decapitalize()
      }
    }

  private class AssertionContextImpl<T>(
    private val assertion: Assertion<T>,
    private val result: Result
  ) : AssertionContext<T>, ComposedAssertionContext<T> {
    override val subject = assertion.subject.value

    override fun pass() {
      if (assertion.negated) {
        result.fail()
        if (assertion.mode == FAIL_FAST) {
          assertion.subject.throwOnFailure()
        }
      } else {
        result.pass()
      }
    }

    override fun fail() {
      if (assertion.negated) {
        result.pass()
      } else {
        result.fail()
        if (assertion.mode == FAIL_FAST) {
          assertion.subject.throwOnFailure()
        }
      }
    }

    override fun fail(failure: Failure) {
      if (assertion.negated) {
        result.pass()
      } else {
        result.fail(failure)
        if (assertion.mode == FAIL_FAST) {
          assertion.subject.throwOnFailure()
        }
      }
    }

    override val allPassed: Boolean
      get() = result.allPassed

    override val anyPassed: Boolean
      get() = result.anyPassed

    override val allFailed: Boolean
      get() = result.allFailed

    override val anyFailed: Boolean
      get() = result.anyFailed

    override infix fun then(block: ComposedAssertionContext<T>.() -> Unit): Assertion<T> {
      this.block()
      return assertion
    }
  }
}
