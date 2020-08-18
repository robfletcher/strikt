package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import strikt.api.Assertion
import strikt.api.expectThat

internal object CollectionAssertions : JUnit5Minutests {
  fun tests() = rootContext {
    derivedContext<Assertion.Builder<Collection<Any?>>>("hasSize") {
      fixture { expectThat(setOf("catflap", "rubberplant", "marzipan")) }

      test("passes if the subject is the expected size") {
        hasSize(3)
      }

      test("fails if the subject is not the expected size") {
        assertThrows<AssertionError> {
          hasSize(1)
        }
      }
    }

    derivedContext<Assertion.Builder<Collection<Any?>>>("isEmpty") {
      context("an empty collection subject") {
        fixture { expectThat(emptyList<Any>()) }

        test("the assertion passes") {
          isEmpty()
        }
      }

      context("an non-empty collection subject") {
        fixture { expectThat(listOf("catflap", "rubberplant", "marzipan")) }

        test("the assertion fails") {
          assertThrows<AssertionError> {
            isEmpty()
          }
        }
      }
    }

    derivedContext<Assertion.Builder<Collection<Any?>>>("isNotEmpty") {
      context("an empty collection subject") {
        fixture { expectThat(emptyList<Any>()) }

        test("the assertion fails") {
          assertThrows<AssertionError> {
            isNotEmpty()
          }
        }
      }

      context("an non-empty collection subject") {
        fixture { expectThat(listOf("catflap", "rubberplant", "marzipan")) }

        test("the assertion passes") {
          isNotEmpty()
        }
      }
    }

    derivedContext<Assertion.Builder<Collection<Any?>>>("isSortedOnAny") {
      context("an empty collection subject") {
        fixture { expectThat(emptyList()) }

        test("the assertion passes") {
          isSorted(Comparator.comparingInt(Any?::hashCode))
        }
      }
    }

    context("isSortedOnInt") {
      context("an un-ordered collection subject") {
        context("fails in a block assertion") {
          assertThrows<AssertionError> {
            expectThat(listOf(1, 3, 2)) {
              isSorted(Comparator.naturalOrder())
            }
          }
        }

        context("fails in a chained assertion") {
          assertThrows<AssertionError> {
            expectThat(listOf(1, 3, 2))
              .isSorted(Comparator.naturalOrder())
          }
        }
      }
    }

    derivedContext<Assertion.Builder<Collection<String?>>>("isSortedOnString") {
      context("an non-empty simple value collection subject") {
        fixture { expectThat(listOf("catflap", "marzipan", "rubber")) }

        test("the assertion passes") {
          isSorted(Comparator.naturalOrder<String>())
        }

        test("fails if the subject is not sorted according to the comparator") {
          assertThrows<AssertionError> {
            isSorted(Comparator.naturalOrder<String>().reversed())
          }
        }
      }

      context("an non-empty simple value collection subject containing null value") {
        fixture { expectThat(listOf("catflap", "marzipan", null)) }

        test("the assertion passes if the Null value is handled through the Comparator instance") {
          isSorted(Comparator.nullsLast(Comparator.naturalOrder<String>()))
        }

        test("fails with NPE if the Null value isn't handled through the Comparator instances") {
          assertThrows<NullPointerException> {
            isSorted(Comparator.naturalOrder<String>())
          }
        }
      }
    }

    derivedContext<Assertion.Builder<Collection<Collection<String>?>>>("isSortedOnNonComparable") {
      context("an non-empty non Comparable value collection subject") {
        fixture {
          expectThat(
            listOf(
              listOf("catflap"),
              listOf("marzipan", "persipan"),
              listOf("rubberplan", "rubber bush", "rubber tree")
            )
          )
        }

        test("the assertion passes") {
          isSorted(Comparator.comparing { it?.size ?: 0 })
        }

        test("fails if the subject is not sorted according to the comparator") {
          assertThrows<AssertionError> {
            isSorted(Comparator.comparing(Collection<String>::size).reversed())
          }
        }
      }
    }
  }
}
