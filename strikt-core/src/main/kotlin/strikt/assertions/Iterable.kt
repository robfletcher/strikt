package strikt.assertions

import strikt.api.Assertion

/**
 * Maps this assertion to an assertion over the first element in the subject
 * iterable.
 *
 * @see Iterable.first
 */
fun <T : Iterable<E>, E> Assertion<T>.first(): Assertion<E> =
  map("first element %s") { first() }

/**
 * Maps this assertion to an assertion over the last element in the subject
 * iterable.
 *
 * @see Iterable.last
 */
fun <T : Iterable<E>, E> Assertion<T>.last(): Assertion<E> =
  map("last element %s") { last() }

/**
 * Asserts that all elements of the subject pass the assertions in [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.all(predicate: Assertion<E>.() -> Unit): Assertion<T> =
  compose("all elements match:") {
    subject.forEach {
      expect(it, predicate)
    }
  } then {
    if (allPassed) pass() else fail()
  }

/**
 * Asserts that _at least one_ element of the subject pass the assertions in
 * [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.any(predicate: Assertion<E>.() -> Unit): Assertion<T> =
  compose("at least one element matches:") {
    subject.forEach {
      expect(it, predicate)
    }
  } then {
    if (anyPassed) pass() else fail()
  }

/**
 * Asserts that _no_ elements of the subject pass the assertions in [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.none(predicate: Assertion<E>.() -> Unit): Assertion<T> =
  compose("no elements match:") {
    subject.forEach {
      expect(it, predicate)
    }
  } then {
    if (allFailed) pass() else fail()
  }

/**
 * Asserts that all [elements] are present in the subject.
 * The elements may exist in any order any number of times and the subject may
 * contain further elements that were not specified.
 * If either the subject or [elements] are empty the assertion always fails.
 */
fun <T : Iterable<E>, E> Assertion<T>.contains(vararg elements: E): Assertion<T> {
  if (elements.isEmpty()) {
    throw IllegalArgumentException("You must supply some expected elements.")
  }
  return compose("contains the elements %s", elements) {
    elements.forEach { element ->
      expect(subject).assert("contains %s", element) {
        if (subject.contains(element)) {
          pass()
        } else {
          fail(actual = element)
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
fun <T : Iterable<E>, E> Assertion<T>.doesNotContain(vararg elements: E): Assertion<T> {
  if (elements.isEmpty()) {
    throw IllegalArgumentException("You must supply some expected elements.")
  }
  return compose("does not contain any of the elements %s", elements) {
    elements.forEach { element ->
      expect(subject).assert("%s does not contain %s", element) {
        if (subject.contains(element)) {
          fail(actual = element)
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
fun <T : Iterable<E>, E> Assertion<T>.containsExactly(vararg elements: E): Assertion<T> =
  compose("contains exactly the elements %s", elements) {
    val original = subject.toList()
    val remaining = subject.toMutableList()
    elements.forEachIndexed { i, element ->
      assert("contains %s", element) {
        if (remaining.remove(element)) {
          pass()
          assert("…at index $i") {
            if (original[i] == element) {
              pass()
            } else {
              fail(actual = original[i])
            }
          }
        } else {
          fail()
        }
      }
    }
    assert("contains no further elements") {
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
 * Asserts that all [elements] _and no others_ are present in the subject.
 * Order is not evaluated, so an assertion on a [List] will pass so long as it
 * contains all the same elements with the same cardinality as [elements]
 * regardless of what order they appear in.
 */
fun <T : Iterable<E>, E> Assertion<T>.containsExactlyInAnyOrder(vararg elements: E): Assertion<T> =
  compose("contains exactly the elements %s in any order") {
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
    assert("contains no further elements") {
      if (remaining.isEmpty()) {
        pass()
      } else {
        fail(actual = remaining)
      }
    }
  } then {
    if (allPassed) pass() else fail()
  }

