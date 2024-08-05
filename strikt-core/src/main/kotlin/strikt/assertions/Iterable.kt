package strikt.assertions

import strikt.api.Assertion.Builder
import strikt.internal.iterable.ElementWithOrderingConstraints
import strikt.internal.iterable.OrderingConstraint
import strikt.internal.iterable.OrderingConstraintsAssertScopeImpl
import strikt.internal.iterable.SectionAssertionSpec

/**
 * Maps this assertion to an assertion over the count of elements in the subject.
 *
 * @see Iterable.count
 */
fun Builder<out Iterable<*>>.count(): Builder<Int> = get(Iterable<*>::count)

/**
 * Maps this assertion to an assertion over the count of elements matching [predicate].
 *
 * @see Iterable.count
 */
fun <T : Iterable<E>, E> Builder<T>.count(
  description: String,
  predicate: (E) -> Boolean,
): Builder<Int> = get("count matching $description") { count(predicate) }

/**
 * Applies [Iterable.map] with [function] to the subject and returns an
 * assertion builder wrapping the result.
 */
infix fun <T : Iterable<E>, E, R> Builder<T>.map(function: (E) -> R): Builder<Iterable<R>> = get { map(function) }

/**
 * Maps this assertion to an assertion over the first element in the subject
 * iterable.
 *
 * @see Iterable.first
 */
fun <T : Iterable<E>, E> Builder<T>.first(): Builder<E> = get("first element %s") { first() }

/**
 * Runs a group of assertions on the first element in the subject iterable.
 *
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return this builder, to facilitate chaining.
 */
infix fun <T : Iterable<E>, E> Builder<T>.withFirst(block: Builder<E>.() -> Unit): Builder<T> =
  with("first element %s", Iterable<E>::first, block)

/**
 * Maps this assertion to an assertion over the indexed element in the subject
 * iterable.
 *
 * @see Iterable.elementAt
 */
fun <T : Iterable<E>, E> Builder<T>.elementAt(index: Int): Builder<E> = get("element at index $index %s") { elementAt(index) }

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
): Builder<T> = with("element at index $index %s", { elementAt(index) }, block)

/**
 * Maps this assertion to an assertion over the single element in the subject
 * iterable.
 *
 * @see Iterable.single
 */
fun <T : Collection<E>, E> Builder<T>.single(): Builder<E> =
  assert("has only one element") {
    if (it.size == 1) {
      pass(it.size)
    } else {
      fail(it)
    }
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
): Builder<T> = with("first matching element %s", { first(predicate) }, block)

/**
 * Maps this assertion to an assertion over the last element in the subject
 * iterable.
 *
 * @see Iterable.last
 */
fun <T : Iterable<E>, E> Builder<T>.last(): Builder<E> = get("last element %s") { last() }

/**
 * Runs a group of assertions on the last element in the subject iterable.
 *
 * @param block a closure that can perform multiple assertions that will all
 * be evaluated regardless of whether preceding ones pass or fail.
 * @return this builder, to facilitate chaining.
 */
fun <T : Iterable<E>, E> Builder<T>.withLast(block: Builder<E>.() -> Unit): Builder<T> = with("last element %s", Iterable<E>::last, block)

fun <T : Iterable<E>, E> Builder<T>.withSingle(block: Builder<E>.() -> Unit): Builder<T> =
  with("single element %s", Iterable<E>::single, block)

/**
 * Maps this assertion to an assertion over a flattened list of the results of
 * [transform] for each element in the subject iterable.
 *
 * @see Iterable.flatMap
 */
infix fun <T : Iterable<E>, E, R> Builder<T>.flatMap(transform: (E) -> Iterable<R>): Builder<List<R>> = get { flatMap(transform) }

/**
 * Maps this assertion to an assertion over a list of all elements of the subject that match `predicate`.
 *
 * @see Iterable.filter
 */
infix fun <T : Iterable<E>, E> Builder<T>.filter(predicate: (E) -> Boolean): Builder<List<E>> = get { filter(predicate) }

/**
 * Maps this assertion to an assertion over a list of all elements of the subject that do not match `predicate`.
 *
 * @see Iterable.filter
 */
infix fun <T : Iterable<E>, E> Builder<T>.filterNot(predicate: (E) -> Boolean): Builder<List<E>> = get { filterNot(predicate) }

/**
 * Maps this assertion to an assertion over a list of all elements of the subject that are instances of `R`.
 *
 * @see Iterable.filterIsInstance
 */
inline fun <reified R> Builder<out Iterable<*>>.filterIsInstance(): Builder<List<R>> = get { filterIsInstance(R::class.java) }

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
infix fun <T : Iterable<E>, E> Builder<T>.one(predicate: Builder<E>.() -> Unit): Builder<T> = exactly(1, predicate)

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
fun <T : Iterable<E>, E> Builder<T>.contains(vararg elements: E): Builder<T> = contains(elements.toList())

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
fun <T : Iterable<E>, E> Builder<T>.doesNotContain(vararg elements: E): Builder<T> = doesNotContain(elements.toList())

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
fun <T : Iterable<E>, E> Builder<T>.containsExactly(vararg elements: E): Builder<T> = containsExactly(elements.toList())

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
fun <T : Iterable<E>, E> Builder<T>.containsExactlyInAnyOrder(vararg elements: E): Builder<T> = containsExactlyInAnyOrder(elements.toList())

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
    val failed =
      (0 until actual.count() - 1).fold(false) { notSorted, index ->
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
fun <T : Iterable<E>, E : Comparable<E>> Builder<T>.isSorted() = isSorted(Comparator.naturalOrder())

/**
 * Similar to [contains], but allows for asserting elements
 * based on their ordering in relation to other elements.
 *
 * This can be used to match fuzzy ordering rules on the result iterable.
 * If the exact order is known, consider [containsExactly].
 * If the order doesn't matter, consider [contains] or [containsExactlyInAnyOrder].
 *
 * In [build], assert elements using [expect][OrderingConstraintsAssertScope.expect],
 * and assert constraints on their positions using the functions in [OrderingConstraintsAssertScope].
 * Constraints can declare that an element appears somewhere before, after, or adjacent to
 * another element. Constraints can also declare that an element appears at the beginning or end
 * of the result iterable.
 *
 * Any number of constraints can be asserted on each element.
 *
 * Results must not contain duplicates, as equality is used to find elements' positions.
 * If a result is expected to contain duplicates, consider using
 * [startNewSection][OrderingConstraintsAssertScope.startNewSection]
 * to assert another set of elements and constraints after the previous set.
 *
 * Examples:
 * ```kotlin
 * expectThat(result).containsWithOrderingConstraints {
 *   // Asserts that "a" is present and is the first element
 *   expect("a").first()
 *
 *   // Asserts that "b" is present and appears immediately after the last expected element, which is "a"
 *   expect("b").immediatelyAfterPrevious()
 *
 *   // Asserts that "c" is present at any position
 *   expect("c")
 *
 *   // Asserts that "d" appears anywhere after "b" and anywhere before "e"
 *   expect("d")
 *     .after("b")
 *     .before("e")
 *
 *   // Asserts that "e" appears anywhere immediately between "d" and "f"
 *   expect("e")
 *     .immediatelyAfter("d")
 *     .immediatelyBefore("f")
 *
 *   // Asserts that "f" is present and is the final element
 *   expect("f").last()
 *
 *   // If we optionally start a new section, we can assert new elements and constraints
 *   // that appear after everything we asserted previously, and are completely separate.
 *   // This can be useful if we expect our result to contain duplicates
 *   startNewSection()
 *
 *   // Asserts that "g" is present and appears immediately after everything expected
 *   // in the last section ("a" through "f")
 *   expect("g").first()
 *
 *   // Asserts that all elements have been declared with `expect` and nothing else is present.
 *   // This is optional.
 *   // If we don't call this, extra elements can still be present after our last expected element.
 *   expectNoFurtherElements()
 * }
 * ```
 */
fun <T: Iterable<E>, E> Builder<T>.containsWithOrderingConstraints(
  build: OrderingConstraintsAssertScope<E>.() -> Unit,
): Builder<T> {

  fun Builder<out Iterable<E>>.assertSectionConstraints(
    sectionConstraints: List<ElementWithOrderingConstraints<E>>,
  ) {
    compose("contains no duplicates") {
      subject.forEach { element ->
        assertThat("%s has no duplicates", element) { subject.count { it == element } == 1 }
      }
    } then { if (allPassed) pass() else fail() }

    val section = subject.toList()
    sectionConstraints.forEachIndexed { sectionConstraintIndex, (element, constraints) ->
      val elementIndex = section.indexOf(element)
      get { element }.run {
        assertThat("is in list") { elementIndex != -1 }
        if (elementIndex == -1) {
          return@run
        }
        constraints.forEach { constraint ->
          when (constraint) {
            OrderingConstraint.First -> {
              assertThat("is first") {
                elementIndex == 0
              }
            }

            OrderingConstraint.Last -> {
              assertThat("is last") {
                elementIndex == section.lastIndex
              }
            }

            is OrderingConstraint.Before -> {
              assert("is before %s", constraint.target) {
                val targetIndex = section.indexOf(constraint.target)
                if (targetIndex == -1) {
                  fail(constraint.target, "%s not in list")
                } else if (elementIndex < targetIndex) {
                  pass()
                } else {
                  fail()
                }
              }
            }

            is OrderingConstraint.ImmediatelyBefore -> {
              assert("is immediately before %s", constraint.target) {
                val targetIndex = section.indexOf(constraint.target)
                if (targetIndex == -1) {
                  fail(constraint.target, "%s not in list")
                } else if (elementIndex == targetIndex - 1) {
                  pass()
                } else {
                  fail()
                }
              }
            }

            is OrderingConstraint.After -> {
              assert("is after %s", constraint.target) {
                val targetIndex = section.indexOf(constraint.target)
                if (targetIndex == -1) {
                  fail(constraint.target, "%s not in list")
                } else if (elementIndex > targetIndex) {
                  pass()
                } else {
                  fail()
                }
              }
            }

            is OrderingConstraint.ImmediatelyAfter -> {
              assert("is immediately after %s", constraint.target) {
                val targetIndex = section.indexOf(constraint.target)
                if (targetIndex == -1) {
                  fail(constraint.target, "%s not in list")
                } else if (elementIndex == targetIndex + 1) {
                  pass()
                } else {
                  fail()
                }
              }
            }

            OrderingConstraint.ImmediatelyAfterPrevious -> {
              val target = sectionConstraints[sectionConstraintIndex - 1].element
              assertThat("is after %s", target) {
                elementIndex > section.indexOf(target)
              }
            }
          }
        }
      }
    }
  }

  val builder = OrderingConstraintsAssertScopeImpl<E>()
  build(builder)
  builder.endSectionIfInProgress()

  val allSections = builder.sections
  return compose(
    "contains the elements %s with custom ordering constraints",
    allSections.flatMap { it.elementsWithConstraints.map { it.element } }
  ) {
    var elementsConsumed = 0
    if (allSections.size == 1) {
      val onlySection = allSections.single()
      assertSectionConstraints(onlySection.elementsWithConstraints)
      elementsConsumed = onlySection.elementsWithConstraints.size
    } else {
      allSections.forEach { section ->
        val sectionConstraints = section.elementsWithConstraints
        val sectionElementCount: Int
        when (val endDefinedBy = section.endDefinedBy) {
          SectionAssertionSpec.EndDefinition.DeclaredElementCount -> {
            // Define the section by the number of elements declared with `expect`
            sectionElementCount = sectionConstraints.size
            get("section %s") { drop(elementsConsumed).take(sectionElementCount) }
              .assert("has %s elements", sectionElementCount) {
                if (it.size == sectionElementCount) {
                  pass()
                } else {
                  fail(it.size, "only %s elements left in list")
                }
              }
              .and { this.assertSectionConstraints(sectionConstraints) }
          }
          is SectionAssertionSpec.EndDefinition.DeclaredElement -> {
            // Define the section by taking everything until the end element
            val remainingElements = subject.drop(elementsConsumed)
            val indexOfEndElement = remainingElements.indexOf(endDefinedBy.element)
            sectionElementCount = if (indexOfEndElement == -1) 0 else indexOfEndElement + 1
            assertThat("contains section ending with %s", endDefinedBy.element) {
              indexOfEndElement != -1
            }
              .and {
                get("section %s") { drop(elementsConsumed).take(sectionElementCount) }
                  .assertSectionConstraints(sectionConstraints)
              }
          }
        }
        elementsConsumed += sectionElementCount
      }
    }

    val assertNoFurtherElements = builder.expectsNoFurtherElements ||
      // Check if the last section explicitly defines the end element
      allSections.last().endDefinedBy is SectionAssertionSpec.EndDefinition.DeclaredElement<*>
    if (assertNoFurtherElements) {
      assert("contains no further elements", expected = emptyList<E>()) {
        if (elementsConsumed == it.count()) {
          pass()
        } else {
          fail(actual = it.drop(elementsConsumed))
        }
      }
    }
  } then {
    if (allPassed) pass() else fail()
  }
}

/** Scope used in [containsWithOrderingConstraints] */
interface OrderingConstraintsAssertScope<E> {

  /**
   * Asserts that [element] exists in the subject iterable.
   *
   * Assertions on its position relative to other elements
   * can be made with the returned [OrderingConstraintsBuilder].
   */
  fun expect(element: E): OrderingConstraintsBuilder<E>

  /**
   * Asserts that all elements have been declared with [expect] and nothing else is present.
   *
   * If this is not called, extra elements can still be present after the last expected element.
   */
  fun expectNoFurtherElements()

  /**
   * Virtually splits the subject iterable into a new section.
   * Each section of elements has its elements and constraints asserted separately from other sections,
   * and they are evaluated sequentially.
   *
   * Assertions made with [expect] after this call will function as if they are on a
   * sub-list of the subject iterable.
   *
   * This can be useful if the result is expected to contain duplicates.
   * For example, consider an iterable that ends with the same element it starts with,
   * and has various elements in between that don't matter:
   * ```kotlin
   * enum class State { Idle, Busy, Unknown }
   *
   * expectThat(resultStates).containsWithOrderingConstraints {
   *   expect(State.Idle).first()
   *   expect(State.Busy)
   *
   *   startNewSection()
   *   expect(State.Idle).last()
   * }
   * ```
   * This asserts that the list of result states:
   * - Begins and ends with `Idle`
   * - Contains `Busy` at least once in between
   * - Does not contain `Idle` in between
   */
  fun startNewSection()
}

/**
 * Builder for asserting position constraints on an element.
 * Retrieved by calling [expect][OrderingConstraintsAssertScope.expect]
 * inside [containsWithOrderingConstraints].
 */
interface OrderingConstraintsBuilder<E> {

  /**
   * Asserts that this element appears before all other elements.
   *
   * If [startNewSection][OrderingConstraintsAssertScope.startNewSection] was previously called,
   * the assertion will be that this element is the first element in the new section,
   * implying it appears immediately after the last element of the previous section.
   */
  fun first(): OrderingConstraintsBuilder<E>

  /**
   * Asserts that this element appears after all other elements.
   *
   * If [startNewSection][OrderingConstraintsAssertScope.startNewSection] is called after this,
   * the assertion will be that this element is the last element in the entire subject iterable.
   *
   * Otherwise, if no calls to [startNewSection][OrderingConstraintsAssertScope.startNewSection]
   * are made after this,
   * the assertion will be that this element is the last element of the current section,
   * implying it appears immediately before the first element of the next section.
   *
   * To assert the end of the subject iterable without declaring which element is last,
   * use [expectNoFurtherElements][OrderingConstraintsAssertScope.expectNoFurtherElements].
   */
  fun last(): OrderingConstraintsBuilder<E>

  /**
   * Asserts that this element appears anywhere before [other] in the subject iterable.
   *
   * This does not assert that the elements are adjacent. For that, use [immediatelyBefore].
   *
   * If [startNewSection][OrderingConstraintsAssertScope.startNewSection] is called,
   * this will only find instances of [other] within those calls.
   */
  fun before(other: E): OrderingConstraintsBuilder<E>

  /**
   * Asserts that this element appears immediately before [other].
   *
   * To instead assert that this element appears anywhere before [other], use [before].
   */
  fun immediatelyBefore(other: E): OrderingConstraintsBuilder<E>

  /**
   * Asserts that this element appears anywhere after [other] in the subject iterable.
   *
   * This does not assert that the elements are adjacent. For that, use [immediatelyAfter].
   *
   * If [startNewSection][OrderingConstraintsAssertScope.startNewSection] is called,
   * this will only find instances of [other] within those calls.
   */
  fun after(other: E): OrderingConstraintsBuilder<E>

  /**
   * Asserts that this element appears immediately after [other].
   *
   * To instead assert that this element appears anywhere after [other], use [after].
   */
  fun immediatelyAfter(other: E): OrderingConstraintsBuilder<E>

  /**
   * Asserts that this element appears immediately after the element previously asserted
   * with [expect][OrderingConstraintsAssertScope].
   * This is a convenience for calling [immediatelyAfter] with the same argument as the last
   * call to [expect][OrderingConstraintsAssertScope].
   */
  fun immediatelyAfterPrevious(): OrderingConstraintsBuilder<E>
}

