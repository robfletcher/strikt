package kirk.api

import kirk.assertions.isA
import kirk.internal.AssertionResultCollector
import kirk.internal.FailFastAssertionResultHandler

/**
 * Start a chain of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subject the subject of the chain of assertions.
 * @return an assertion for [subject].
 */
fun <T> expect(subject: T): Assertion<T> = expect("%s", subject)

/**
 * Start a chain of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subjectDescription a description for [subject] with a [String.format]
 * style placeholder for the value itself.
 * @param subject the subject of the chain of assertions.
 * @return an assertion for [subject].
 */
fun <T> expect(subjectDescription: String, subject: T): Assertion<T> {
  val reporter = FailFastAssertionResultHandler()
  return Assertion(reporter, subjectDescription, subject)
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
fun <T> expect(subject: T, block: Assertion<T>.() -> Unit): Assertion<T> =
  expect("%s", subject, block)

/**
 * Evaluate a block of assertions over [subject].
 * This is the entry-point for the assertion API.
 *
 * @param subjectDescription a description for [subject] with a [String.format]
 * style placeholder for the value itself.
 * @param subject the subject of the block of assertions.
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return an assertion for [subject].
 */
fun <T> expect(
  subjectDescription: String,
  subject: T,
  block: Assertion<T>.() -> Unit
): Assertion<T> {
  val reporter = AssertionResultCollector()
  return Assertion(reporter, subjectDescription, subject)
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
): Assertion<E> = throws("", action)

/**
 * Asserts that [action] throws an exception of type [E] when executed.
 *
 * @param description a description of [action].
 * @return an assertion over the thrown exception, allowing further assertions
 * about messages, root causes, etc.
 */
inline fun <reified E : Throwable> throws(
  description: String,
  action: () -> Unit
): Assertion<E> =
  try {
    action()
    null
  } catch (e: Throwable) {
    e
  }.let { thrown ->
    // TODO: check the message produced here for usefulness
    expect(description, thrown).isA()
  }
