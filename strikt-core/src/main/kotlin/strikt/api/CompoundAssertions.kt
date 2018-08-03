package strikt.api

interface CompoundAssertions<T> {

  infix fun then(block: CompoundAssertion<T>.() -> Unit): Asserter<T>

}
