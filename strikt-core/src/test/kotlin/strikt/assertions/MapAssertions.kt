package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.expectThat

@DisplayName("assertions on Map")
internal object MapAssertions : JUnit5Minutests {
  fun mapAssertions() = rootContext<Assertion.Builder<Map<String, String>>> {
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

      test("get mapping returns a null subject") {
        get("foo").isNull()
      }
    }

    context("a non-empty map") {
      fixture {
        expectThat(
          mapOf(
            "foo" to "bar",
            "baz" to "fnord",
            "qux" to "fnord"
          )
        )
      }

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

      context("get mapping") {
        test("returns an assertion over the value for a valid key") {
          get("foo").isEqualTo("bar")
        }

        test("returns a null subject for a non-existent key") {
          get("bar").isNull()
        }
      }

      context("getValue mapping") {
        test("returns an assertion over the value for a valid key") {
          getValue("foo").isEqualTo("bar")
        }

        test("fails for a non-existent key") {
          assertThrows<AssertionFailedError> {
            getValue("bar").isEqualTo("this will never get evaluated")
          }.also {
            expectThat(it.message)
              .isEqualTo(
                """
                |▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
                |  ✗ has an entry with the key "bar"
              """.trimMargin()
              )
          }
        }
      }
    }
  }
}
