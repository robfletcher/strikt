package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

@DisplayName("assertions on Map")
internal object MapAssertions {
  @TestFactory
  fun mapAssertions() = assertionTests<Map<String, String>> {
    context("an empty subject") {
      fixture { expectThat(emptyMap()) }

      test("isEmpty assertion passes") {
        isEmpty()
      }

      test("isNotEmpty assertion fails") {
        assertThrows<AssertionError> {
          isNotEmpty()
        }
      }
    }

    context("a non-empty map") {
      fixture { expectThat(mapOf("foo" to "bar", "baz" to "fnord", "qux" to "fnord")) }

      test("isEmpty assertion fails") {
        assertThrows<AssertionError> {
          isEmpty()
        }
      }

      test("isNotEmpty assertion passes") {
        isNotEmpty()
      }

      context("containsKey assertion") {
        test("passes if the subject has a matching key") {
          containsKey("foo")
        }

        test("fails if the subject does not have a matching key") {
          val error = assertThrows<AssertionError> {
            containsKey("bar")
          }
          assertEquals(
            """▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
              |  ✗ has an entry with the key "bar""""
              .trimMargin(),
            error.message
          )
        }
      }

      context("containsKeys assertion") {
        test("passes if the subject has all the specified keys") {
          containsKeys("foo", "baz")
        }

        test("fails if the subject does not have a matching key") {
          val error = assertThrows<AssertionError> {
            containsKeys("foo", "bar", "fnord")
          }
          assertEquals(
            """▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
              |  ✗ has entries with the keys ["foo", "bar", "fnord"]
              |    ✓ has an entry with the key "foo"
              |    ✗ has an entry with the key "bar"
              |    ✗ has an entry with the key "fnord""""
              .trimMargin(),
            error.message
          )
        }
      }

      context("hasEntry assertion") {
        test("passes if the subject has a matching key value pair") {
          hasEntry("foo", "bar")
        }

        test("fails if the subject does not have a matching key") {
          val error = assertThrows<AssertionError> {
            hasEntry("bar", "foo")
          }
          assertEquals(
            """▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
              |  ✗ has an entry with the key "bar""""
              .trimMargin(),
            error.message
          )
        }

        test("fails if the subject has a different value associated with the key") {
          val error = assertThrows<AssertionError> {
            hasEntry("foo", "baz")
          }
          assertEquals(
            """▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
              |  ✓ has an entry with the key "foo"
              |  ▼ entry ["foo"]:
              |    ✗ is equal to "baz" : found "bar""""
              .trimMargin(),
            error.message
          )
        }
      }
    }
  }
}
