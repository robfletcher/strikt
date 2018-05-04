package kirk.api

/**
 * Holds a subject of type [T] that you can then make assertions about.
 */
interface Assertion<T> {
  // TODO: these only make sense to be visible inside an assertion function, not sure how to hide them from other contexts
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
  fun assert(description: String, assertion: AssertionContext<T>.() -> Unit): Assertion<T>

  /**
   * Maps the assertion subject to the result of [function].
   * For example, if [function] is a property reference on [T]
   */
  // TODO: not sure about this name, it's fundamentally similar to Kotlin's run. Also it might be nice to have a dedicated `map` for Assertion<Iterable>.
  fun <R> map(function: T.() -> R): Assertion<R>
}

/**
 * Allows reporting of success or failure by assertion implementations.
 *
 * This class is the receiver of the lambda passed to [Assertion.assert].
 *
 * @see Assertion.assert
 */
interface AssertionContext<T> {
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

  /**
   * Start a chain of assertions under the current group.
   *
   * Simpler assertions will not need this but if you want to make
   * "sub-assertions" about multiple properties of an object, or all elements of
   * a collection for example, you can do so with `expect` and then use
   * [allFailed], [anyFailed], [allPassed] and [anyPassed] to make a decision
   * about the overall result.
   * The results of assertions chained from this method are included under the
   * overall assertion result.
   *
   * @param subject the subject of the chain of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @return an assertion for [subject].
   */
  fun <E> expect(subject: E): Assertion<E>

  /**
   * Evaluate a block of assertions under the current group.
   *
   * Simpler assertions will not need this but if you want to make
   * "sub-assertions" about multiple properties of an object, or all elements of
   * a collection for example, you can do so with `expect` and then use
   * [allFailed], [anyFailed], [allPassed] and [anyPassed] to make a decision
   * about the overall result.
   * The results of assertions chained from this method are included under the
   * overall assertion result.
   *
   * @param subject the subject of the block of assertions, usually a property
   * or element of the subject of the surrounding assertion.
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   * @return an assertion for [subject].
   */
  fun <E> expect(subject: E, block: Assertion<E>.() -> Unit): Assertion<E>

  /**
   * Returns `true` if any nested assertions evaluated using [expect] failed,
   * `false` otherwise.
   *
   * @see expect
   */
  val anyFailed: Boolean
  /**
   * Returns `true` if _all_ nested assertions evaluated using [expect] failed,
   * `false` otherwise.
   *
   * @see expect
   */
  val allFailed: Boolean
  /**
   * Returns `true` if any nested assertions evaluated using [expect] passed,
   * `false` otherwise.
   *
   * @see expect
   */
  val anyPassed: Boolean
  /**
   * Returns `true` if _all_ nested assertions evaluated using [expect] passed,
   * `false` otherwise.
   *
   * @see expect
   */
  val allPassed: Boolean
}
