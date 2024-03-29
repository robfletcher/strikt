package strikt.api

import kotlinx.coroutines.runBlocking
import strikt.api.Assertion.Builder
import strikt.assertions.failedWith
import strikt.assertions.isSuccess
import strikt.internal.AssertionBuilder
import strikt.internal.AssertionStrategy.Collecting
import strikt.internal.AssertionStrategy.Throwing
import strikt.internal.AssertionStrategy.Throwing.evaluate
import strikt.internal.AssertionSubject
import strikt.internal.DefaultExpectationBuilder

/**
 * Starts a block of assertions that will all be evaluated regardless of whether
 * earlier ones fail.
 * This is the entry-point for the assertion API.
 */
fun expect(block: suspend ExpectationBuilder.() -> Unit) {
  val subjects = mutableListOf<AssertionSubject<*>>()
  DefaultExpectationBuilder(subjects)
    .apply {
      runBlocking {
        block()
      }
    }
    .let {
      evaluate(subjects)
    }
}

/**
 * Start a chain of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subject the subject of the chain of assertions.
 * @return an assertion for [subject].
 */
fun <T> expectThat(subject: T): DescribeableBuilder<T> = AssertionBuilder(AssertionSubject(subject), Throwing)

/**
 * Evaluate a block of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subject the subject of the block of assertions.
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return an assertion for [subject].
 */
fun <T> expectThat(
  subject: T,
  block: Builder<T>.() -> Unit
) {
  AssertionSubject(subject).let { context ->
    AssertionBuilder(context, Collecting).apply(block)
    evaluate(context)
  }
}

/**
 * Asserts that [action]throws an exception of type [E] when executed.
 *
 * @return an assertion over the thrown exception, allowing further assertions
 * about messages, root causes, etc.
 */
inline fun <reified E : Throwable> expectThrows(noinline action: suspend () -> Any?): Builder<E> = expectCatching(action).failedWith()


/**
 * Asserts that [action] does not throw an exception when executed.
 *
 * @return an assertion for result of [action].
 */
fun <T> expectDoesNotThrow(
  action: suspend () -> T
) = expectCatching(action).isSuccess()

/**
 * Start a chain of assertions over the result of [action] which may either be
 * the value [action] returns or any exception it throws.
 * This is the entry-point for the assertion API.
 *
 * @return an assertion for the successful or failed result of [action].
 */
fun <T : Any?> expectCatching(action: suspend () -> T): DescribeableBuilder<Result<T>> =
  expectThat(
    runCatching {
      runBlocking { action() }
    }
  )
