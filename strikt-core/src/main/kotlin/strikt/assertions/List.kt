package strikt.assertions

import strikt.api.Asserter

/**
 * Maps this assertion to an assertion on the element at index [i] in the
 * subject list.
 */
operator fun <T : List<E>, E> Asserter<T>.get(i: Int): Asserter<E> =
  map("element [$i] %s") { get(i) }

/**
 * Maps this assertion to an assertion on the elements at the sub-list
 * represented by [range] in the subject list.
 */
operator fun <T : List<E>, E> Asserter<T>.get(range: IntRange): Asserter<List<E>> =
  map("elements [$range] %s") { subList(range.first, range.last + 1) }
