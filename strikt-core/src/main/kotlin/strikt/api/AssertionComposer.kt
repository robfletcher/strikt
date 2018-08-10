package strikt.api

import strikt.api.Assertion.Builder

/**
 * Allows assertions to be composed, or nested.
 * This class is the receiver of the lambda passed to
 * [Builder.compose].
 *
 * @property subject The subject of the assertion.
 */
interface AssertionComposer<T> : Builder<T> {
  val subject: T
}
