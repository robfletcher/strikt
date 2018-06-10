package strikt.api

import strikt.api.Mode.COLLECT
import strikt.api.reporting.Result
import strikt.api.reporting.Subject

/**
 * Allows assertions to be composed, or nested.
 * This class is the receiver of the lambda passed to
 * [Assertion.compose].
 *
 * @property subject The subject of the assertion.
 */
class ComposedAssertions<T>
internal constructor(
  private val parent: Subject<T>,
  private val result: Result
) {
  val subject = parent.value

  /**
   * Start a chain of assertions in the current nested context.
   *
   * @param subject the subject of the chain of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @return an assertion for [subject].
   */
  fun <E> expect(subject: E): Assertion<E> =
    Subject(subject)
      .also(result::append)
      .let { Assertion(it, COLLECT) }

  /**
   * Evaluate a block of assertions in the current nested context.
   *
   * @param subject the subject of the block of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   * @return an assertion for [subject].
   */
  fun <E> expect(
    subject: E,
    block: Assertion<E>.() -> Unit
  ): Assertion<E> =
    Subject(subject)
      .also(result::append)
      .let { Assertion(it, COLLECT).apply(block) }

  /**
   * Evaluates a composed assertion on the original subject.
   * This creates a new assertion in the composed context using the same
   * subject as the overall assertion.
   * This is useful because it allows for the overall assertion to contain much
   * more detail in any failure message.
   *
   * @param description a description for the conditions the assertion evaluates.
   * @param assertion the assertion implementation that should result in a call
   * to [AssertionContext.pass] or [AssertionContext.fail].
   * @return this assertion, in order to facilitate a fluent API.
   */
  fun assert(description: String, assertion: AssertionContext<T>.() -> Unit) =
    parent.copy().let {
      result.append(it)
      Assertion(it, COLLECT).assert(description, assertion)
    }

  /**
   * Evaluates a composed assertion on the original subject.
   * This creates a new assertion in the composed context using the same
   * subject as the overall assertion.
   * This is useful because it allows for the overall assertion to contain much
   * more detail in any failure message.
   *
   * @param description a description for the condition the assertion evaluates.
   * @param expected the expected value of a comparison.
   * @param assertion the assertion implementation that should result in a call
   * to [AssertionContext.pass] or [AssertionContext.fail].
   * @return this assertion, in order to facilitate a fluent API.
   */
  fun assert(
    description: String,
    expected: Any?,
    assertion: AssertionContext<T>.() -> Unit
  ) =
    parent.copy().let {
      result.append(it)
      Assertion(it, COLLECT).assert(description, expected, assertion)
    }
}
