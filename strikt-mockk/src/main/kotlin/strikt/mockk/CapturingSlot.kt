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
  get() = get { if (isCaptured) captured else error("No value has been captured") }
