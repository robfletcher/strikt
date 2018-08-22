package strikt.assertions

import strikt.api.Assertion

@get:JvmName("first_pair")
val <A, B> Assertion.Builder<Pair<A, B>>.first: Assertion.Builder<A>
  get() = map(Pair<A, B>::first)

@get:JvmName("second_pair")
val <A, B> Assertion.Builder<Pair<A, B>>.second: Assertion.Builder<B>
  get() = map(Pair<A, B>::second)

@get:JvmName("first_triple")
val <A, B, C> Assertion.Builder<Triple<A, B, C>>.first: Assertion.Builder<A>
  get() = map(Triple<A, B, C>::first)

@get:JvmName("second_triple")
val <A, B, C> Assertion.Builder<Triple<A, B, C>>.second: Assertion.Builder<B>
  get() = map(Triple<A, B, C>::second)

val <A, B, C> Assertion.Builder<Triple<A, B, C>>.third: Assertion.Builder<C>
  get() = map(Triple<A, B, C>::third)
