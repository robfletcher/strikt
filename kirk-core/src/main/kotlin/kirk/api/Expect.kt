package kirk.api

import kirk.assertions.isA
import kirk.internal.AggregatingReporter
import kirk.internal.AssertionFailed
import kirk.internal.FailFastReporter

/**
 * Start a chain of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subject the subject of the chain of assertions.
 * @return an assertion for [subject].
 */
fun <T> expect(subject: T): Assertion<T> {
  val reporter = FailFastReporter()
  return Assertion(reporter, subject)
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
fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T> {
  val reporter = AggregatingReporter()
  return Assertion(reporter, subject)
    .apply(block)
    .apply {
      if (reporter.anyFailed) {
        throw AssertionFailed(reporter.results)
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
  action: () -> Unit
): Assertion<E> {
  val thrown = try {
    action()
    null
  } catch (e: Throwable) {
    e
  }
  return expect(thrown).isA()
}
