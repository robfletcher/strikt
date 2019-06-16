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
  get() = get(Throwable::message).isNotNull()

/**
 * Maps an assertion on a [Throwable] to an assertion on its [Throwable.cause].
 */
val <T : Throwable> Builder<T>.cause: DescribeableBuilder<Throwable?>
  get() = get(Throwable::cause)

/**
 * Asserts that the result of an action threw an exception of the expected type.
 * The assertion fails if the subject's [Result.isSuccess] returns `true` or the
 * exception is not an instance of [E].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified E : Throwable> Builder<Result<*>>.throws(): Builder<E> =
  assert("threw %s", E::class.java) {
    val exception = it.exceptionOrNull()
    when {
      it.isSuccess -> fail("nothing was thrown")
      exception is E -> pass()
      else -> fail(
        description = "threw %s",
        actual = exception,
        cause = exception
      )
    }
  }
    .get("thrown exception") { requireNotNull(exceptionOrNull() as E?) }

/**
 * Asserts that the result of an action did not throw any exception and maps to
 * an assertion on the result value. The assertion fails if the subject's
 * [Result.isFailure] returns `true`.
 */
@Suppress("UNCHECKED_CAST")
fun <R : Any> Builder<Result<R>>.doesNotThrow(): Builder<R> =
  assert("did not throw an exception") {
    when {
      it.isSuccess -> pass()
      else -> fail(
        description = "threw %s",
        actual = it.exceptionOrNull(),
        cause = it.exceptionOrNull()
      )
    }
  }
    .get { requireNotNull(getOrNull()) }
