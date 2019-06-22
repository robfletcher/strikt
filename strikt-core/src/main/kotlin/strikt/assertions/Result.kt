package strikt.assertions

import strikt.api.Assertion
import strikt.api.DescribeableBuilder
import strikt.api.Failure
import strikt.api.Success
import strikt.api.Try

/**
 * Asserts that the subject is a successful result and maps this assertion to
 * an assertion over the result value.
 *
 * @see [Result.isSuccess].
 */
inline fun <reified T : Any?> Assertion.Builder<Try<T>>.succeeded(): DescribeableBuilder<Success<T>> {
  assert("succeeded") { subject ->
    when (subject) {
      is Success -> pass()
      is Failure -> fail(cause = subject.exception)
    }
  }
  return this as DescribeableBuilder<Success<T>>
}

/**
 * Asserts that the subject is a failed result and maps this assertion to an
 * assertion over the exception that was thrown
 *
 * @see [Result.isFailure].
 */
fun <T : Any?> Assertion.Builder<Try<T>>.failed(): DescribeableBuilder<Failure> {
  assert("failed") { subject ->
    when (subject) {
      is Success -> fail(actual = subject.value)
      is Failure -> pass()
    }
  }
  return this as DescribeableBuilder<Failure>
}

val <T : Any?> Assertion.Builder<Success<T>>.value: DescribeableBuilder<T>
  get() = get { value }

val Assertion.Builder<Failure>.exception: DescribeableBuilder<Throwable>
  get() = get { exception }

/**
 * Asserts that the subject is a isFailure result that threw an exception
 * assignable to [E] and maps this assertion to an assertion over that
 * exception.
 *
 * @see [Result.isFailure].
 */
inline fun <reified E : Throwable> Assertion.Builder<Try<*>>.failedWith() =
  failed().isA<E>()
