package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Maps an assertion on a [Throwable] to an assertion on its message.
 * This mapping also asserts that the message is not `null`.
 * This is particularly useful after [throws].
 *
 * @author [Xavier Hanin](https://github.com/xhanin)
 */
val <T : Throwable> Builder<T>.message: Builder<String>
  get() = map(Throwable::message).isNotNull()
