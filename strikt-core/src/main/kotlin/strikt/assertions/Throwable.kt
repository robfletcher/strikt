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
val <T : Throwable> Builder<T>.message: Builder<String>
  get() = traverse(Throwable::message).isNotNull()

/**
 * Maps an assertion on a [Throwable] to an assertion on its [Throwable.cause].
 */
val <T : Throwable> Builder<T>.cause: DescribeableBuilder<Throwable?>
  get() = traverse(Throwable::cause)

/**
 * Asserts that an exception is an instance of the expected type.
 * The assertion fails if the subject is `null` or not an instance of [E].
 *
 * This assertion is designed for use with the [strikt.api.catching] function.
 */
inline fun <reified E : Throwable> Builder<Throwable?>.throws(): Builder<E> =
  assert("threw %s", E::class.java) {
    when (it) {
      null -> fail("nothing was thrown")
      is E -> pass()
      else -> fail(description = "%s was thrown", actual = it, cause = it)
    }
  } as Builder<E>
