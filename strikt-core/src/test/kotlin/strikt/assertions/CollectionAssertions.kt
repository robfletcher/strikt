package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

@DisplayName("assertions on Collection")
internal class CollectionAssertions {
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
}
