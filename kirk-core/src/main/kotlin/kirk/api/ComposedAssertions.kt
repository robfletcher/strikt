package kirk.api

import kirk.internal.Mode

/**
 * Allows assertions to be composed, or nested, using [AssertionContext.compose].
 */
class ComposedAssertions<T>
internal constructor(
  private val parent: Subject<T>,
  private val result: Result
) {
  /**
   * Start a chain of assertions in the current nested context.
   *
   * @param subject the subject of the chain of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @return an assertion for [subject].
   */
  fun <E> expect(subject: E): Assertion<E> = expect("Expect that %s", subject)

  /**
   * Start a chain of assertions in the current nested context.
   *
   * @param description a description for [subject] with a [String.format] style
   * placeholder for the value itself.
   * @param subject the subject of the chain of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @return an assertion for [subject].
   */
  fun <E> expect(description: String, subject: E): Assertion<E> =
    Subject(description, subject)
      .also(result::append)
      .let { Assertion(it, Mode.COLLECT) }

  /**
   * Evaluate a block of assertions in the current nested context.
   *
   * @param subject the subject of the block of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   * @return an assertion for [subject].
   */
  fun <E> expect(subject: E, block: Assertion<E>.() -> Unit): Assertion<E> =
    expect("Expect that %s", subject, block)

  /**
   * Evaluate a block of assertions in the current nested context.
   *
   * @param description a description for [subject] with a [String.format] style
   * placeholder for the value itself.
   * @param subject the subject of the block of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   * @return an assertion for [subject].
   */
  fun <E> expect(
    description: String,
    subject: E,
    block: Assertion<E>.() -> Unit
  ): Assertion<E> =
    Subject(description, subject)
      .also(result::append)
      .let { Assertion(it, Mode.COLLECT).apply(block) }

  /**
   * Evaluates a composed assertion on the original subject.
   * This creates a new assertion in the composed context using the same
   * subject as the overall assertion.
   * This is useful because it allows for the overall assertion to contain much
   * more detail in any failure message.
   */
  fun assert(description: String, assertion: AssertionContext<T>.() -> Unit) =
    parent.copy().let {
      result.append(it)
      Assertion(it, Mode.COLLECT).assert(description, assertion)
    }
}
