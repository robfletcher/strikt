package strikt.mockk

import io.mockk.CapturingSlot
import strikt.api.Assertion

/**
 * Asserts that the subject has captured a value.
 *
 * @see CapturingSlot.isCaptured
 */
fun <T : Any> Assertion.Builder<CapturingSlot<T>>.isCaptured(): Assertion.Builder<CapturingSlot<T>> =
  assertThat("captured a value", CapturingSlot<T>::isCaptured)

/**
 * Maps this assertion to an assertion whose subject is the captured value of
 * the [CapturingSlot].
 *
 * @see CapturingSlot.captured
 */
val <T : Any> Assertion.Builder<CapturingSlot<T>>.captured: Assertion.Builder<T>
  get() = get("captured value %s") { if (isCaptured) captured else error("No value has been captured") }

/**
 * Runs a group of assertions on the captured value.
 *
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return this builder, to facilitate chaining.
 */
fun <T : Any> Assertion.Builder<CapturingSlot<T>>.withCaptured(
  block: Assertion.Builder<T>.() -> Unit
): Assertion.Builder<CapturingSlot<T>> =
  with("captured value %s", CapturingSlot<T>::captured, block)
