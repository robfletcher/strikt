package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Maps this assertion to an assertion over the count of elements in the subject.
 *
 * @see Iterable.count
 */
fun Builder<out Iterable<*>>.count(): Builder<Int> =
  get(Iterable<*>::count)

/**
 * Maps this assertion to an assertion over the count of elements matching [predicate].
 *
 * @see Iterable.count
 */
fun <T : Iterable<E>, E> Builder<T>.count(
  description: String,
  predicate: (E) -> Boolean,
): Builder<Int> =
  get("count matching $description") { count(predicate) }

/**
 * Applies [Iterable.map] with [function] to the subject and returns an
 * assertion builder wrapping the result.
 */
infix fun <T : Iterable<E>, E, R> Builder<T>.map(function: (E) -> R): Builder<Iterable<R>> =
  get { map(function) }

/**
 * Maps this assertion to an assertion over the first element in the subject
 * iterable.
 *
 * @see Iterable.first
 */
fun <T : Iterable<E>, E> Builder<T>.first(): Builder<E> =
  get("first element %s") { first() }

/**
 * Runs a group of assertions on the first element in the subject iterable.
 *
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return this builder, to facilitate chaining.
 */
fun <T : Iterable<E>, E> Builder<T>.withFirst(block: Builder<E>.() -> Unit): Builder<T> =
  with("first element %s", Iterable<E>::first, block)

/**
 * Maps this assertion to an assertion over the indexed element in the subject
 * iterable.
 *
 * @see Iterable.elementAt
 */
fun <T : Iterable<E>, E> Builder<T>.elementAt(index: Int): Builder<E> =
  get("element at index $index %s") { elementAt(index) }

/**
 * Runs a group of assertions on the element at [index] in the subject iterable.
 *
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return this builder, to facilitate chaining.
 */
fun <T : Iterable<E>, E> Builder<T>.withElementAt(
  index: Int,
  block: Builder<E>.() -> Unit,
): Builder<T> =
  with("element at index $index %s", { elementAt(index) }, block)

/**
 * Maps this assertion to an assertion over the single element in the subject
 * iterable.
 *
 * @see Iterable.single
 */
fun <T : Collection<E>, E> Builder<T>.single(): Builder<E> =
  assert("has only one element") {
    if (it.size == 1) pass(it.size)
    else fail(it)
  }
    .get("single element %s") { single() }

/**
 * Maps this assertion to an assertion over the first element in the subject
 * iterable that matches [predicate].
 *
 * @see Iterable.first
 * @throws strikt.internal.opentest4j.MappingFailed if no elements match [predicate].
 */
infix fun <T : Iterable<E>, E> Builder<T>.first(predicate: (E) -> Boolean): Builder<E> =
  get("first matching element %s") { first(predicate) }

/**
 * Runs a group of assertions on the first element in the subject iterable that
 * matches [predicate].
 *
 * @see Iterable.first
 * @throws strikt.internal.opentest4j.MappingFailed if no elements match [predicate].
 */
fun <T : Iterable<E>, E> Builder<T>.withFirst(
  predicate: (E) -> Boolean,
  block: Builder<E>.() -> Unit,
): Builder<T> =
  with("first matching element %s", { first(predicate) }, block)

/**
 * Maps this assertion to an assertion over the last element in the subject
 * iterable.
 *
 * @see Iterable.last
 */
fun <T : Iterable<E>, E> Builder<T>.last(): Builder<E> =
  get("last element %s") { last() }

/**
 * Runs a group of assertions on the last element in the subject iterable.
 *
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return this builder, to facilitate chaining.
 */
fun <T : Iterable<E>, E> Builder<T>.withLast(block: Builder<E>.() -> Unit): Builder<T> =
  with("last element %s", Iterable<E>::last, block)

/**
 * Maps this assertion to an assertion over a flattened list of the results of
 * [transform] for each element in the subject iterable.
 *
 * @see Iterable.flatMap
 */
infix fun <T : Iterable<E>, E, R> Builder<T>.flatMap(transform: (E) -> Iterable<R>): Builder<List<R>> =
  get { flatMap(transform) }

/**
 * Maps this assertion to an assertion over a list of all elements of the subject that match `predicate`.
 *
 * @see Iterable.filter
 */
infix fun <T : Iterable<E>, E> Builder<T>.filter(predicate: (E) -> Boolean): Builder<List<E>> =
  get { filter(predicate) }

/**
 * Maps this assertion to an assertion over a list of all elements of the subject that do not match `predicate`.
 *
 * @see Iterable.filter
 */
infix fun <T : Iterable<E>, E> Builder<T>.filterNot(predicate: (E) -> Boolean): Builder<List<E>> =
  get { filterNot(predicate) }

/**
 * Maps this assertion to an assertion over a list of all elements of the subject that are instances of `R`.
 *
 * @see Iterable.filterIsInstance
 */
inline fun <reified R> Builder<out Iterable<*>>.filterIsInstance(): Builder<List<R>> =
  get { filterIsInstance(R::class.java) }

/**
 * Asserts that all elements of the subject pass the assertions in [predicate].
 */
infix fun <T : Iterable<E>, E> Builder<T>.all(predicate: Builder<E>.() -> Unit): Builder<T> =
  compose("all elements match:") { subject ->
    subject.forEach { element ->
      get("%s") { element }.apply(predicate)
    }
  } then {
    if (allPassed) pass() else fail()
  }

/**
 * Asserts that all elements of the subject pass the assertions in [predicate].
 */
infix fun <T : Iterable<E>, E> Builder<T>.allIndexed(predicate: Builder<E>.(Int) -> Unit): Builder<T> =
  compose("all elements match:") { subject ->
    subject.forEachIndexed { index, element ->
      get("%s") { element }.apply { predicate(index) }
    }
  } then {
    if (allPassed) pass() else fail()
  }

/**
 * Asserts that _at least one_ element of the subject pass the assertions in
 * [predicate].
 */
infix fun <T : Iterable<E>, E> Builder<T>.any(predicate: Builder<E>.() -> Unit): Builder<T> =
  compose("at least one element matches:") { subject ->
    subject.forEach { element ->
      get("%s") { element }.apply(predicate)
    }
  } then {
    if (anyPassed) pass() else fail()
  }

/**
 * Asserts that _at least one_ element of the subject pass the assertions in
 * [predicate].
 */
infix fun <T : Iterable<E>, E> Builder<T>.anyIndexed(predicate: Builder<E>.(Int) -> Unit): Builder<T> =
  compose("at least one element matches:") { subject ->
    subject.forEachIndexed { index, element ->
      get("%s") { element }.apply { predicate(index) }
    }
  } then {
    if (anyPassed) pass() else fail()
  }

/**
 * Asserts that _no_ elements of the subject pass the assertions in [predicate].
 */
infix fun <T : Iterable<E>, E> Builder<T>.none(predicate: Builder<E>.() -> Unit): Builder<T> =
  compose("no elements match:") { subject ->
    subject.forEach { element ->
      get("%s") { element }.apply(predicate)
    }
  } then {
    if (allFailed) pass() else fail()
  }

/**
 * Asserts that _no_ elements of the subject pass the assertions in [predicate].
 */
infix fun <T : Iterable<E>, E> Builder<T>.noneIndexed(predicate: Builder<E>.(Int) -> Unit): Builder<T> =
  compose("no elements match:") { subject ->
    subject.forEachIndexed { index, element ->
      get("%s") { element }.apply { predicate(index) }
    }
  } then {
    if (allFailed) pass() else fail()
  }

/**
 * Asserts that _exactly one_ element of the subject passes the assertions in [predicate].
 */
infix fun <T : Iterable<E>, E> Builder<T>.one(predicate: Builder<E>.() -> Unit): Builder<T> =
  exactly(1, predicate)

/**
 * Asserts that at least [count] elements of the subject pass the assertions in
 * [predicate].
 */
fun <T : Iterable<E>, E> Builder<T>.atLeast(
  count: Int,
  predicate: Builder<E>.() -> Unit,
): Builder<T> =
  compose("at least $count elements match:") { subject ->
    subject.forEach { element ->
      get("%s") { element }.apply(predicate)
    }
  } then {
    if (passedCount >= count) pass() else fail()
  }

/**
 * Asserts that at most [count] elements of the subject pass the assertions in
 * [predicate].
 */
fun <T : Iterable<E>, E> Builder<T>.atMost(
  count: Int,
  predicate: Builder<E>.() -> Unit,
): Builder<T> =
  compose("at most $count elements match:") { subject ->
    subject.forEach { element ->
      get("%s") { element }.apply(predicate)
    }
  } then {
    if (passedCount <= count) pass() else fail()
  }

/**
 * Asserts that exactly [count] elements of the subject pass the assertions in
 * [predicate].
 */
fun <T : Iterable<E>, E> Builder<T>.exactly(
  count: Int,
  predicate: Builder<E>.() -> Unit,
): Builder<T> =
  compose("exactly $count elements match:") { subject ->
    subject.forEach { element ->
      get("%s") { element }.apply(predicate)
    }
  } then {
    if (passedCount == count) pass() else fail()
  }

/**
 * Asserts that all [elements] are present in the subject.
 * The elements may exist in any order any number of times and the subject may
 * contain further elements that were not specified.
 * If either the subject or [elements] are empty the assertion always fails.
 */
fun <T : Iterable<E>, E> Builder<T>.contains(vararg elements: E): Builder<T> =
  contains(elements.toList())

/**
 * Asserts that all [elements] are present in the subject.
 * The elements may exist in any order any number of times and the subject may
 * contain further elements that were not specified.
 * If either the subject or [elements] are empty the assertion always fails.
 */
infix fun <T : Iterable<E>, E> Builder<T>.contains(elements: Collection<E>): Builder<T> =
  when {
    elements.isEmpty() ->
      assert("contains the elements %s", elements) {
        pass()
      }
    elements.size == 1 ->
      assert("contains %s", elements.first()) { subject ->
        if (subject.contains(elements.first())) {
          pass()
        } else {
          fail()
        }
      }
    else ->
      compose("contains the elements %s", elements) {
        elements.forEach { element ->
          assert("contains %s", element) { subject ->
            if (subject.contains(element)) {
              pass()
            } else {
              fail()
            }
          }
        }
      } then {
        if (allPassed) pass() else fail()
      }
  }

/**
 * Asserts that none of [elements] are present in the subject.
 *
 * If [elements] is empty the assertion always fails.
 * If the subject is empty the assertion always passe.
 */
fun <T : Iterable<E>, E> Builder<T>.doesNotContain(vararg elements: E): Builder<T> =
  doesNotContain(elements.toList())

/**
 * Asserts that none of [elements] are present in the subject.
 *
 * If [elements] is empty the assertion always fails.
 * If the subject is empty the assertion always passe.
 */
infix fun <T : Iterable<E>, E> Builder<T>.doesNotContain(elements: Collection<E>): Builder<T> =
  when {
    elements.isEmpty() ->
      throw IllegalArgumentException("You must supply some expected elements.")
    elements.size == 1 ->
      assert("does not contain %s", elements.first()) { subject ->
        if (subject.contains(elements.first())) {
          fail()
        } else {
          pass()
        }
      }
    else ->
      compose("does not contain any of the elements %s", elements) {
        elements.forEach { element ->
          assert("does not contain %s", element) { subject ->
            if (subject.contains(element)) {
              fail()
            } else {
              pass()
            }
          }
        }
      } then {
        if (allPassed) pass() else fail()
      }
  }

/**
 * Asserts that all [elements] _and no others_ are present in the subject in the
 * specified order.
 *
 * If the subject has no guaranteed iteration order (for example a [Set]) this
 * assertion is probably not appropriate and you should use
 * [containsExactlyInAnyOrder] instead.
 */
fun <T : Iterable<E>, E> Builder<T>.containsExactly(vararg elements: E): Builder<T> =
  containsExactly(elements.toList())

/**
 * Asserts that all [elements] _and no others_ are present in the subject in the
 * specified order.
 *
 * If the subject has no guaranteed iteration order (for example a [Set]) this
 * assertion is probably not appropriate and you should use
 * [containsExactlyInAnyOrder] instead.
 */
infix fun <T : Iterable<E>, E> Builder<T>.containsExactly(elements: Collection<E>): Builder<T> =
  compose("contains exactly the elements %s", elements.toList()) { subject ->
    val original = subject.toList()
    val remaining = subject.toMutableList()
    elements.forEachIndexed { i, element ->
      assert("contains %s", element) {
        if (remaining.remove(element)) {
          pass()
          assert("…at index $i", element) {
            when {
              i !in original.indices -> fail("index $i is out of range")
              original[i] == element -> pass()
              else -> fail(actual = original[i])
            }
          }
        } else {
          fail()
        }
      }
    }
    assert("contains no further elements", emptyList<E>()) {
      if (remaining.isEmpty()) {
        pass()
      } else {
        fail(actual = remaining.toList())
      }
    }
  } then {
    if (allPassed) pass() else fail()
  }

/**
 * Asserts that all [elements] _and no others_ are present in the subject.
 * Order is not evaluated, so an assertion on a [List] will pass so long as it
 * contains all the same elements with the same cardinality as [elements]
 * regardless of what order they appear in.
 */
fun <T : Iterable<E>, E> Builder<T>.containsExactlyInAnyOrder(vararg elements: E): Builder<T> =
  containsExactlyInAnyOrder(elements.toList())

/**
 * Asserts that all [elements] _and no others_ are present in the subject.
 * Order is not evaluated, so an assertion on a [List] will pass so long as it
 * contains all the same elements with the same cardinality as [elements]
 * regardless of what order they appear in.
 */
infix fun <T : Iterable<E>, E> Builder<T>.containsExactlyInAnyOrder(elements: Collection<E>): Builder<T> =
  compose(
    "contains exactly the elements %s in any order",
    elements.toList()
  ) { subject ->
    val remaining = subject.toMutableList()
    elements.forEach { element ->
      assert("contains %s", element) {
        if (remaining.remove(element)) {
          pass()
        } else {
          fail()
        }
      }
    }
    assert("contains no further elements", emptyList<E>()) {
      if (remaining.isEmpty()) {
        pass()
      } else {
        fail(actual = remaining)
      }
    }
  } then {
    if (allPassed) pass() else fail()
  }

/**
 * Asserts that the subject iterable is sorted according to the Comparator. Empty iterables are
 * considered sorted.
 */
infix fun <T : Iterable<E>, E> Builder<T>.isSorted(comparator: Comparator<E>) =
  assert("is sorted") { actual ->
    val failed = (0 until actual.count() - 1).fold(false) { notSorted, index ->
      if (notSorted || comparator.compare(actual.elementAt(index), actual.elementAt(index + 1)) <= 0) {
        notSorted
      } else {
        fail(actual, "${actual.elementAt(index)} is greater than ${actual.elementAt(index + 1)}")
        true
      }
    }
    if (!failed) pass()
  }

/**
 * Asserts that the subject iterable is sorted according to the natural order of its elements. Empty
 * iterables are considered sorted.
 */
fun <T : Iterable<E>, E : Comparable<E>> Builder<T>.isSorted() =
  isSorted(Comparator.naturalOrder())
