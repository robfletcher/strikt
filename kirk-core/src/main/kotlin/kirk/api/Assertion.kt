package kirk.api

import kirk.internal.Mode
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
   * Evaluates a condition that may pass or fail.
   *
   * This method can be used directly in a test but is typically used inside an
   * extension method on `Assertion<T>` such as those provided in the
   * [kirk.assertions] package.
   *
   * @sample kirk.samples.AssertionMethods.assert
   *
   * @param description a description for the condition the assertion evaluates.
   * @param assertion the assertion implementation that should result in a call
   * to [AssertionContext.pass] or [AssertionContext.fail].
   * @return this assertion, in order to facilitate a fluent API.
   * @see AssertionContext.pass
   * @see AssertionContext.fail
   */
  fun assert(description: String, assertion: AssertionContext<T>.() -> Unit) =
    apply {
      val result = Result(description).also(subject::append)
      val context = object : AssertionContext<T>, ComposedAssertionContext {
        override val subject = this@Assertion.subject.value

        override fun pass() {
          if (negated) {
            result.fail()
            if (mode == Mode.FAIL_FAST) {
              throw AssertionFailed(this@Assertion.subject.root)
            }
          } else {
            result.pass()
          }
        }

        override fun fail() {
          if (negated) {
            result.pass()
          } else {
            result.fail()
            if (mode == Mode.FAIL_FAST) {
              throw AssertionFailed(this@Assertion.subject.root)
            }
          }
        }

        override fun fail(actualDescription: String, actualValue: Any?) {
          if (negated) {
            result.pass()
          } else {
            result.fail(actualDescription, actualValue)
            if (mode == Mode.FAIL_FAST) {
              throw AssertionFailed(this@Assertion.subject.root)
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

        override fun compose(assertions: ComposedAssertions<T>.() -> Unit): ComposedAssertionContext {
          ComposedAssertions(this@Assertion.subject, result).apply(assertions)
          return this
        }
      }
      context.assertion()
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
   * @sample kirk.samples.AssertionMethods.map
   *
   * @param function a lambda whose receiver is the current assertion subject.
   * @return an assertion whose subject is the value returned by [function].
   */
  // TODO: not sure about this name, it's fundamentally similar to Kotlin's run. Also it might be nice to have a dedicated `map` for Assertion<Iterable>.
  fun <R> map(function: T.() -> R): Assertion<R> =
    when (function) {
      is CallableReference -> map(".${function.propertyName} %s", function)
      else                 -> map("%s", function)
    }

  /**
   * Maps the assertion subject to the result of [function].
   * This is useful for chaining to property values or method call results on
   * the subject.
   *
   * @sample kirk.samples.AssertionMethods.map
   *
   * @param description a description of the mapped result.
   * @param function a lambda whose receiver is the current assertion subject.
   * @return an assertion whose subject is the value returned by [function].
   */
  fun <R> map(description: String, function: T.() -> R): Assertion<R> =
    Subject(description, subject.value.function())
      .also(subject::append)
      .let { Assertion(it, mode) }

  /**
   * Reverses any assertions chained after this method.
   *
   * @sample kirk.samples.AssertionMethods.not
   *
   * @return an assertion that negates the results of any assertions applied to
   * its subject.
   */
  fun not(): Assertion<T> = Assertion(subject, mode, !negated)

  private val CallableReference.propertyName: String
    get() = "^get(.+)$".toRegex().find(name).let { match ->
      return when (match) {
        null -> name
        else -> match.groupValues[1].decapitalize()
      }
    }
}
