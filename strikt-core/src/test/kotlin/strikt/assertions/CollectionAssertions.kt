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
  }
}
