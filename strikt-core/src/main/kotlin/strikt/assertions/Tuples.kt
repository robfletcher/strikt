package strikt.assertions

import strikt.api.Assertion

/**
 * Maps an assertion on a [Pair] to an assertion on its [Pair.first] property.
 */
@get:JvmName("first_pair")
val <A, B> Assertion.Builder<Pair<A, B>>.first: Assertion.Builder<A>
  get() = get(Pair<A, B>::first)

/**
 * Maps an assertion on a [Pair] to an assertion on its [Pair.second] property.
 */
@get:JvmName("second_pair")
val <A, B> Assertion.Builder<Pair<A, B>>.second: Assertion.Builder<B>
  get() = get(Pair<A, B>::second)

/**
 * Maps an assertion on a [Triple] to an assertion on its [Triple.first] property.
 */
@get:JvmName("first_triple")
val <A, B, C> Assertion.Builder<Triple<A, B, C>>.first: Assertion.Builder<A>
  get() = get(Triple<A, B, C>::first)

/**
 * Maps an assertion on a [Triple] to an assertion on its [Triple.second] property.
 */
@get:JvmName("second_triple")
val <A, B, C> Assertion.Builder<Triple<A, B, C>>.second: Assertion.Builder<B>
  get() = get(Triple<A, B, C>::second)

/**
 * Maps an assertion on a [Triple] to an assertion on its [Triple.third] property.
 */
val <A, B, C> Assertion.Builder<Triple<A, B, C>>.third: Assertion.Builder<C>
  get() = get(Triple<A, B, C>::third)
