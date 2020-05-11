package strikt.assertions

import strikt.api.Assertion.Builder
import strikt.api.DescribeableBuilder

/**
 * Maps an assertion on a [Throwable] to an assertion on its
 * [Throwable.message].
 * This mapping also asserts that the message is not `null`.
 *
 * @author [Xavier Hanin](https://github.com/xhanin)
 */
val <T : Throwable> Builder<T>.message: Builder<String?>
  get() = get(Throwable::message)

/**
 * Maps an assertion on a [Throwable] to an assertion on its [Throwable.cause].
 */
val <T : Throwable> Builder<T>.cause: DescribeableBuilder<Throwable?>
  get() = get(Throwable::cause)
