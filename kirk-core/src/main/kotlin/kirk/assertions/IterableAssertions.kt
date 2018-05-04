package kirk.assertions

import kirk.api.Assertion
import java.util.*

/**
 * Asserts that all elements of the subject pass the assertions in [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.all(predicate: Assertion<E>.() -> Unit) =
  assert("all elements match predicate") {
    subject.forEach {
      expect(it, predicate)
    }
    if (allPassed) {
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
  assert("at least one element matches predicate") {
    subject.forEach {
      expect(it, predicate)
    }
    if (anyPassed) {
      pass()
    } else {
      fail()
    }
  }

/**
 * Asserts that _no_ elements of the subject pass the assertions in [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.none(predicate: Assertion<E>.() -> Unit) =
  assert("no elements match predicate") {
    subject.forEach {
      expect(it, predicate)
    }
    if (allFailed) {
      pass()
    } else {
      fail()
    }
  }

/**
 * Asserts that all [elements] are present in the subject.
 * The elements may exist in any order any number of times and the subject may
 * contain further elements that were not specified.
 * If either the subject or [elements] are empty the assertion always fails.
 */
fun <T : Iterable<E>, E> Assertion<T>.contains(vararg elements: E) =
  assert("contains the elements ${Arrays.toString(elements)}") {
    when (elements.size) {
      0    -> fail() // TODO: really need a message here
      else -> {
        elements.forEach { element ->
          expect(subject)
            .assert("contains $element") {
              if (subject.contains(element)) {
                pass()
              } else {
                fail()
              }
            }
        }
        if (allPassed) {
          pass()
        } else {
          fail()
        }
      }
    }
  }

// TODO: containsExactly