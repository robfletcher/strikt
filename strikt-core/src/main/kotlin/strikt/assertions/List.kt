package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Maps this assertion to an assertion on the element at index [i] in the
 * subject list.
 */
operator fun <T : List<E>, E> Builder<T>.get(i: Int): Builder<E> =
  get("element [$i] %s") { this[i] }

/**
 * Maps this assertion to an assertion on the elements at the sub-list
 * represented by [range] in the subject list.
 */
operator fun <T : List<E>, E> Builder<T>.get(range: IntRange): Builder<List<E>> =
  get("elements [$range] %s") {
    subList(range.first, range.last + 1)
  }
