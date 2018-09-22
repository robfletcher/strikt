package strikt.assertions

import strikt.api.Assertion

/**
 * Maps an assertion on a [Pair] to an assertion on its [Pair.first] property.
 */
@get:JvmName("first_pair")
val <A, B> Assertion.Builder<Pair<A, B>>.first: Assertion.Builder<A>
  get() = traverse(Pair<A, B>::first)

/**
 * Maps an assertion on a [Pair] to an assertion on its [Pair.second] property.
 */
@get:JvmName("second_pair")
val <A, B> Assertion.Builder<Pair<A, B>>.second: Assertion.Builder<B>
  get() = traverse(Pair<A, B>::second)

/**
 * Maps an assertion on a [Triple] to an assertion on its [Triple.first] property.
 */
@get:JvmName("first_triple")
val <A, B, C> Assertion.Builder<Triple<A, B, C>>.first: Assertion.Builder<A>
  get() = traverse(Triple<A, B, C>::first)

/**
 * Maps an assertion on a [Triple] to an assertion on its [Triple.second] property.
 */
@get:JvmName("second_triple")
val <A, B, C> Assertion.Builder<Triple<A, B, C>>.second: Assertion.Builder<B>
  get() = traverse(Triple<A, B, C>::second)

/**
 * Maps an assertion on a [Triple] to an assertion on its [Triple.third] property.
 */
val <A, B, C> Assertion.Builder<Triple<A, B, C>>.third: Assertion.Builder<C>
  get() = traverse(Triple<A, B, C>::third)
