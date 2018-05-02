package kirk.assertions

import kirk.api.Assertion

/**
 * Asserts that all elements of the subject pass the assertions in [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.all(predicate: Assertion<E>.() -> Unit) =
  nested("all elements match predicate") {
    subject.forEach {
      expect(it, predicate)
    }
    if (allSucceeded) {
      pass()
    } else {
      fail()
    }
  }

/**
 * Asserts that _at least one_ element of the subject pass the assertions in
 * [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.any(predicate: Assertion<E>.() -> Unit) =
  nested("at least one element matches predicate") {
    subject.forEach {
      expect(it, predicate)
    }
    if (anySucceeded) {
      pass()
    } else {
      fail()
    }
  }

/**
 * Asserts that _no_ elements of the subject pass the assertions in [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.none(predicate: Assertion<E>.() -> Unit) =
  nested("no elements match predicate") {
    subject.forEach {
      expect(it, predicate)
    }
    if (allFailed) {
      pass()
    } else {
      fail()
    }
  }