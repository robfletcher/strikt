package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Maps this assertion to an assertion on the element at index [i] in the
 * subject list.
 */
operator fun <T : List<E>, E> Builder<T>.get(i: Int): Builder<E> =
  map("element [$i] %s") { it[i] }

/**
 * Maps this assertion to an assertion on the elements at the sub-list
 * represented by [range] in the subject list.
 */
operator fun <T : List<E>, E> Builder<T>.get(range: IntRange): Builder<List<E>> =
  map("elements [$range] %s") {
    it.subList(range.first, range.last + 1)
  }
