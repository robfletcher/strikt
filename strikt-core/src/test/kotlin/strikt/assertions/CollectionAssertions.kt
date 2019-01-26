package strikt.assertions

import com.oneeyedmen.minutest.junit.toTestFactory
import com.oneeyedmen.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

@DisplayName("assertions on Collection")
internal object CollectionAssertions {
  @TestFactory
  fun hasSize() = assertionTests<Collection<Any?>> {
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

  @TestFactory
  fun isEmpty() = assertionTests<Collection<Any?>> {
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

  @TestFactory
  fun isNotEmpty() = assertionTests<Collection<Any?>> {
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

  @TestFactory
  fun isSortedOnAny() = assertionTests<Collection<Any?>> {
    context("an empty collection subject") {
      fixture { expectThat(emptyList()) }

      test("the assertion passes") {
        isSorted(Comparator.comparingInt(Any::hashCode))
      }
    }
  }

  @TestFactory
  fun isSortedOnInt() = rootContext<Unit> {
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
  }.toTestFactory()

  @TestFactory
  fun isSortedOnString() = assertionTests<Collection<String?>> {
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

  @TestFactory
  fun isSortedOnNonComparable() = assertionTests<Collection<Collection<String>?>> {
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
        isSorted(Comparator.comparing(Collection<String>::size))
      }

      test("fails if the subject is not sorted according to the comparator") {
        assertThrows<AssertionError> {
          isSorted(Comparator.comparing(Collection<String>::size).reversed())
        }
      }
    }
  }
}
