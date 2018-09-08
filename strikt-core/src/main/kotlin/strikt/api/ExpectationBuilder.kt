package strikt.api

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
}
