package strikt.api

import strikt.api.reporting.Subject
import strikt.assertions.throws
import strikt.opentest4j.throwOnFailure

/**
 * Start a chain of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subject the subject of the chain of assertions.
 * @return an assertion for [subject].
 */
fun <T> expect(subject: T): Assertion<T> {
  return Assertion(Subject(subject), Mode.FAIL_FAST)
}

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
  block: Assertion<T>.() -> Unit
): Assertion<T> {
  return Subject(subject)
    .let {
      Assertion(it, Mode.COLLECT).apply {
        block()
        it.throwOnFailure()
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
): Assertion<E> =
  expect(action).throws()
