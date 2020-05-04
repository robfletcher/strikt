package strikt.api

/**
 * Receiver for [expect] providing functions that define assertion subjects and
 * create assertion chains or blocks.
 */
interface ExpectationBuilder {

  /**
   * Start a chain of assertions over [subject].
   *
   * @param subject the subject of the chain of assertions.
   * @return an assertion for [subject].
   */
  fun <T> that(subject: T): DescribeableBuilder<T>

  /**
   * Evaluate a block of assertions over [subject].
   *
   * @param subject the subject of the block of assertions.
   * @param block a closure that can perform multiple assertions that will all
   * be evaluated regardless of whether preceding ones pass or fail.
   * @return an assertion for [subject].
   */
  fun <T> that(
    subject: T,
    block: Assertion.Builder<T>.() -> Unit
  ): DescribeableBuilder<T>

  /**
   * Start a chain of assertions over the result of [action].

   * @param action an action that may result in a value being returned or an
   * exception being thrown.
   * @return an assertion for the result of [action].
   */
  fun <T> catching(action: suspend () -> T): DescribeableBuilder<Result<T>>
}
