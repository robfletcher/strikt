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
fun <T> expect(subject: T): Assertion<T> = expect("Expect that %s", subject)

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
  return Assertion(Subject(subjectDescription, subject), Mode.FAIL_FAST)
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
  expect("Expect that %s", subject, block)

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
  return Subject(subjectDescription, subject)
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

/**
 * Asserts that [action] throws an exception of type [E] when executed.
 *
 * @param description a description of [action].
 * @return an assertion over the thrown exception, allowing further assertions
 * about messages, root causes, etc.
 */
inline fun <reified E : Throwable> throws(
  description: String,
  noinline action: () -> Unit
): Assertion<E> =
  expect(description, action).throws()
