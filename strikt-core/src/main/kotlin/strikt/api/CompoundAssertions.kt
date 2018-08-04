package strikt.api

interface CompoundAssertions<T> {
  /**
   * Determine the overall status of a compound assertion based on the results
   * of its child assertions.
   */
  infix fun then(block: CompoundAssertion<T>.() -> Unit): Asserter<T>
}
