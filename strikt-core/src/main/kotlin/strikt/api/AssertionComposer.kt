package strikt.api

/**
 * Allows assertions to be composed, or nested.
 * This class is the receiver of the lambda passed to
 * [Asserter.compose].
 *
 * @property subject The subject of the assertion.
 */
interface AssertionComposer<T> : Asserter<T> {
  val subject: T
}
