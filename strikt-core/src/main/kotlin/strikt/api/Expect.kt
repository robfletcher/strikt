package strikt.api

import strikt.assertions.throws
import strikt.internal.AsserterImpl
import strikt.internal.AssertionSubject
import strikt.internal.Mode.FAIL_FAST

/**
 * Start a chain of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subject the subject of the chain of assertions.
 * @return an assertion for [subject].
 */
fun <T> expect(subject: T): Asserter<T> =
  AsserterImpl(AssertionSubject(subject), FAIL_FAST)

/**
 * Evaluate a block of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * This is a shortcut for:
 *
 *     expect(subject).evaluate(block)
 *
 * @param subject the subject of the block of assertions.
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return an assertion for [subject].
 *
 * @see Asserter.evaluate
 */
fun <T> expect(
  subject: T,
  block: Asserter<T>.() -> Unit
): Asserter<T> =
  expect(subject).evaluate(block)

/**
 * Asserts that [action] throws an exception of type [E] when executed.
 *
 * @return an assertion over the thrown exception, allowing further assertions
 * about messages, root causes, etc.
 */
inline fun <reified E : Throwable> throws(
  noinline action: () -> Unit
): Asserter<E> =
  expect(action).throws()
