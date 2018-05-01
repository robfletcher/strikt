package assertions.api

interface Assertion<T> {
  fun evaluate(description: String, predicate: AtomicAssertionContext.(T) -> Unit)
  fun evaluateNested(description: String, predicate: NestedAssertionContext.(T) -> Unit)
}

interface AtomicAssertionContext {
  fun success()
  fun failure()
}

interface NestedAssertionContext : AtomicAssertionContext {
  fun <T> expect(subject: T): Assertion<T>
  fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T>
  val anyFailed: Boolean
  val allFailed: Boolean
  val anySucceeded: Boolean
  val allSucceeded: Boolean
}
