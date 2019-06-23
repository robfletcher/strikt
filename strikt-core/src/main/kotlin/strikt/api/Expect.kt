package strikt.api

import kotlinx.coroutines.runBlocking
import strikt.api.Assertion.Builder
import strikt.assertions.failedWith
import strikt.internal.AssertionBuilder
import strikt.internal.AssertionStrategy.Collecting
import strikt.internal.AssertionStrategy.Throwing
import strikt.internal.AssertionSubject

/**
 * Starts a block of assertions that will all be evaluated regardless of whether
 * earlier ones fail.
 * This is the entry-point for the assertion API.
 */
fun expect(block: suspend ExpectationBuilder.() -> Unit) {
  val subjects = mutableListOf<AssertionSubject<*>>()
  object : ExpectationBuilder {
    override fun <T> that(subject: T): DescribeableBuilder<T> =
      AssertionSubject(subject)
        .also { subjects.add(it) }
        .let { AssertionBuilder(it, Collecting) }

    override fun <T> that(
      subject: T,
      block: Builder<T>.() -> Unit
    ): DescribeableBuilder<T> = that(subject).apply(block)

    override fun <T> catching(
      action: suspend () -> T
    ): DescribeableBuilder<Result<T>> =
      that(runCatching { runBlocking { action() } })
  }
    .apply {
      runBlocking {
        block()
      }
    }
    .let {
      Throwing.evaluate(subjects)
    }
}

/**
 * Start a chain of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subject the subject of the chain of assertions.
 * @return an assertion for [subject].
 */
fun <T> expectThat(subject: T): DescribeableBuilder<T> =
  AssertionBuilder(AssertionSubject(subject), Throwing)

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
): DescribeableBuilder<T> =
  AssertionSubject(subject).let { context ->
    AssertionBuilder(context, Collecting)
      .apply {
        block()
        Throwing.evaluate(context)
      }
  }

/**
 * Asserts that [action]throws an exception of type [E] when executed.
 *
 * @return an assertion over the thrown exception, allowing further assertions
 * about messages, root causes, etc.
 */
inline fun <reified E : Throwable> expectThrows(
  noinline action: suspend () -> Any?
): Builder<E> =
  expectCatching(action)
    .failedWith()

/**
 * Start a chain of assertions over the result of [action] which may either be
 * the value [action] returns or any exception it throws.
 * This is the entry-point for the assertion API.
 *
 * @return an assertion for the successful or failed result of [action].
 */
fun <T : Any?> expectCatching(action: suspend () -> T): DescribeableBuilder<Try<T>> =
  expectThat(
    try {
      runBlocking { action() }.let(::Success)
    } catch (e: Throwable) {
      Failure(e)
    }
  )
