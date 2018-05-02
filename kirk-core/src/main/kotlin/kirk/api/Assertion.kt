package kirk.api

/**
 * Holds a subject of type [T] that you can then make assertions about.
 */
interface Assertion<T> {
  // TODO: these only make sense to be visible inside an assertion function, not sure how to hide them from other contexts
  /**
   * Used by assertion implementations to evaluate a single condition that may
   * pass or fail.
   *
   * @param description a description for the assertion.
   * @param assertion the assertion implementation that should result in a call
   * to [AtomicAssertionContext.pass] or [AtomicAssertionContext.fail].
   * @return this assertion, in order to facilitate a fluent API.
   */
  fun atomic(description: String, assertion: AtomicAssertionContext<T>.() -> Unit): Assertion<T>

  /**
   * Used by assertion implementations to evaluate a group of conditions that
   * may individually pass or fail and result in the passing or failure of the
   * overall assertion.
   *
   * @param description a description for the overall assertion.
   * @param assertions the assertion implementation that should perform a group
   * of assertions using [NestedAssertionContext.expect] and finall result in a
   * call to [NestedAssertionContext.pass] or
   * [NestedAssertionContext.fail].
   * @return this assertion, in order to facilitate a fluent API.
   */
  fun nested(description: String, assertions: NestedAssertionContext<T>.() -> Unit): Assertion<T>

  /**
   * Maps the assertion subject to the result of [function].
   * For example, if [function] is a property reference on [T]
   */
  // TODO: not sure about this name, it's fundamentally similar to Kotlin's run. Also it might be nice to have a dedicated `map` for Assertion<Iterable>.
  fun <R> map(function: T.() -> R): Assertion<R>
}

/**
 * Allows reporting of success or failure by assertion implementations.
 */
interface AtomicAssertionContext<T> {
  /**
   * The assertion subject.
   */
  val subject: T

  /**
   * Report that the assertion succeeded.
   */
  fun pass()

  /**
   * Report that the assertion failed.
   */
  fun fail()
}

/**
 * Allows grouping of assertions under a single main assertion and reporting of
 * success or failure.
 * This class is used for more complex assertions such as on all elements of a
 * collection or multiple fields of an object.
 */
interface NestedAssertionContext<T> : AtomicAssertionContext<T> {
  /**
   * Start a chain of assertions under the current group.
   *
   * @param subject the subject of the chain of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @return an assertion for [subject].
   */
  fun <E> expect(subject: E): Assertion<E>

  /**
   * Evaluate a block of assertions under the current group.
   *
   * @param subject the subject of the block of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   * @return an assertion for [subject].
   */
  fun <E> expect(subject: E, block: Assertion<E>.() -> Unit): Assertion<E>

  /**
   * Returns `true` if any assertions in this group failed, `false` otherwise.
   */
  val anyFailed: Boolean
  /**
   * Returns `true` if _all_ assertions in this group failed, `false` otherwise.
   */
  val allFailed: Boolean
  /**
   * Returns `true` if any assertions in this group passed, `false` otherwise.
   */
  val anySucceeded: Boolean
  /**
   * Returns `true` if _all_ assertions in this group passed, `false` otherwise.
   */
  val allSucceeded: Boolean
}
