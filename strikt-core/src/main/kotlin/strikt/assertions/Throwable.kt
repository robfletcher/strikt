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

/**
 * Asserts that the result of an action did throw an exception and maps to
 * an assertion on the exception. The assertion fails if the subject's
 * [Result.isFailure] returns `false`.
 *
 * @see runCatching
 *
 * @author [Bengt Brodersen](https://github.com/qoomon)
 */
fun <R> Builder<Result<R>>.isFailure(): Builder<Throwable> =
  assert("is Failure") {
    when {
      it.isFailure -> pass()
      else -> fail(
        description = "returned %s",
        actual = it.getOrThrow()
      )
    }
  }
    .get("exception") {
      exceptionOrNull()!!
    }

/**
 * Asserts that the result of an action did not throw any exception and maps to
 * an assertion on the result value. The assertion fails if the subject's
 * [Result.isSuccess] returns `false`.
 *
 * @see runCatching
 *
 * @author [Bengt Brodersen](https://github.com/qoomon)
 */
fun <R> Builder<Result<R>>.isSuccess(): Builder<R> =
  assert("is Success") {
    when {
      it.isSuccess -> pass()
      else -> fail(
        description = "threw %s",
        actual = it.exceptionOrNull(),
        cause = it.exceptionOrNull()
      )
    }
  }
    .get("value") {
      // WORKAROUND - Handle inline class bug. (This will also work when this bug is fixed)
      val value = getOrThrow()
      if (value is Result<*>)
        @Suppress("UNCHECKED_CAST")
        return@get value.getOrThrow() as R
      // WORKAROUND - END

      getOrThrow()
    }


/**
 * Deprecated form of [isSuccess]`()`.
 *
 * @see isSuccess()
 */
@Deprecated(
  "Use isSuccess instead",
  replaceWith = ReplaceWith("isSuccess()")
)
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
