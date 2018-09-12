package strikt.api

import strikt.api.Assertion.Builder
import strikt.api.Status.Failed
import strikt.assertions.throws
import strikt.internal.AssertionBuilder
import strikt.internal.AssertionStrategy
import strikt.internal.AssertionSubject
import strikt.internal.opentest4j.AtomicAssertionFailure
import strikt.internal.opentest4j.CompoundAssertionFailure
import strikt.internal.reporting.writePartialToString
import strikt.internal.reporting.writeToString

/**
 * Start a chain of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subject the subject of the chain of assertions.
 * @return an assertion for [subject].
 */
fun <T> expect(subject: T): DescribeableBuilder<T> =
  AssertionBuilder(AssertionSubject(subject), AssertionStrategy.Throwing)

/**
 * Evaluate a block of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subject the subject of the block of assertions.
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return an assertion for [subject].
 */
fun <T> expect(
  subject: T,
  block: Builder<T>.() -> Unit
): DescribeableBuilder<T> =
  AssertionSubject(subject).let { context ->
    AssertionBuilder(context, AssertionStrategy.Collecting)
      .apply {
        block()
        if (context.status is Failed) {
          throw CompoundAssertionFailure(
            context.root.writeToString(),
            context
              .children
              .filter { it.status is Failed }
              .map { AtomicAssertionFailure(it.writePartialToString(), it) }
          )
        }
      }
  }

/**
 * Asserts that [action] throws an exception of type [E] when executed.
 *
 * @return an assertion over the thrown exception, allowing further assertions
 * about messages, root causes, etc.
 */
inline fun <reified E : Throwable> throws(
  noinline action: () -> Unit
): Builder<E> =
  expect(action).throws()

/**
 * special case expect method to fix blocks that don't return Unit
 */
fun expect(subject: () -> Unit): DescribeableBuilder<() -> Unit> =
  AssertionBuilder(AssertionSubject(subject), AssertionStrategy.Throwing)
