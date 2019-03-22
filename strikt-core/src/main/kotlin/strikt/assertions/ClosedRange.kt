package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Maps an assertion on the [ClosedRange] to an assertion on its [ClosedRange.start].
 */
val <T : ClosedRange<E>, E> Builder<T>.start: Builder<E>
  get() =
    get { start }

/**
 * Maps an assertion on the [ClosedRange] to an assertion on its [ClosedRange.endInclusive].
 */
val <T : ClosedRange<E>, E> Builder<T>.endInclusive: Builder<E>
  get() =
    get { endInclusive }

/**
 * Asserts that the subject range contains the provided [element].
 */
fun <T : ClosedRange<E>, E> Builder<T>.contains(element: E): Builder<T> =
  assert("contains %s", element) {
    when (element) {
      in it -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the subject range is empty.
 */
fun <T : ClosedRange<E>, E> Builder<T>.isEmpty(): Builder<T> =
  assert("is empty") {
    when {
      it.isEmpty() -> pass()
      else -> fail()
    }
  }
