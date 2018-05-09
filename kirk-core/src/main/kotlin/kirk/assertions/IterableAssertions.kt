package kirk.assertions

import kirk.api.Assertion

/**
 * Asserts that all elements of the subject pass the assertions in [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.all(predicate: Assertion<E>.() -> Unit) =
  assert("all elements match predicate") {
    compose {
      subject.forEach {
        expect(it, predicate)
      }
    } results {
      if (allPassed) pass() else fail()
    }
  }

/**
 * Asserts that _at least one_ element of the subject pass the assertions in
 * [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.any(predicate: Assertion<E>.() -> Unit) =
  assert("at least one element matches predicate") {
    compose {
      subject.forEach {
        expect(it, predicate)
      }
    } results {
      if (anyPassed) pass() else fail()
    }
  }

/**
 * Asserts that _no_ elements of the subject pass the assertions in [predicate].
 */
fun <T : Iterable<E>, E> Assertion<T>.none(predicate: Assertion<E>.() -> Unit) =
  assert("no elements match predicate") {
    compose {
      subject.forEach {
        expect(it, predicate)
      }
    } results {
      if (allFailed) pass() else fail()
    }
  }

/**
 * Asserts that all [elements] are present in the subject.
 * The elements may exist in any order any number of times and the subject may
 * contain further elements that were not specified.
 * If either the subject or [elements] are empty the assertion always fails.
 */
fun <T : Iterable<E>, E> Assertion<T>.contains(vararg elements: E) =
  assert("contains the elements ${elements.toList()}") {
    when (elements.size) {
      0    -> fail() // TODO: really need a message here
      else -> {
        compose {
          elements.forEach { element ->
            expect(subject).assert("contains $element") {
              if (subject.contains(element)) {
                pass()
              } else {
                fail()
              }
            }
          }
        } results {
          if (allPassed) pass() else fail()
        }
      }
    }
  }

/**
 * Asserts that all [elements] _and no others_ are present in the subject.
 * If the subject is an ordered iterable such as a [List] or array then the
 * elements must be ordered the same way.
 */
fun <T : Iterable<E>, E> Assertion<T>.containsExactly(vararg elements: E) =
  assert("contains exactly the elements ${elements.toList()}") {
    compose {
      when (subject) {
        is List<*>       -> expect(subject.toList())
          .hasSize(elements.size)
          .isEqualTo(elements.toList())
        is Collection<*> -> expect(subject.toList())
          .hasSize(elements.size)
          .contains(*elements)
        else             -> {
          val subjectIterator = subject.iterator()
          val expectedIterator = elements.iterator()
          if (!expectedIterator.hasNext()) {
            // TODO: fail
          } else {
            while (expectedIterator.hasNext()) {
              if (subjectIterator.hasNext()) {
                expect(subjectIterator.next()).isEqualTo(expectedIterator.next())
              } else {
                // TODO: fail
                break
              }
            }
            if (subjectIterator.hasNext()) {
              // TODO: fail
            }
          }
        }
      }
    } results {
      if (allPassed) pass() else fail()
    }
  }

// TODO: containsInOrder
