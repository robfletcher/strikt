package strikt.api

import strikt.api.Assertion.Builder

/**
 * Returned by [Assertion.Builder.compose] allowing a bridge between evaluation
 * of composed assertions and the determination of the overall result in the
 * `block` parameter passed to [then].
 */
interface CompoundAssertions<T> {
  /**
   * Determine the overall status of a compound assertion based on the results
   * of its child assertions.
   */
  infix fun then(block: CompoundAssertion.() -> Unit): Builder<T>
}
