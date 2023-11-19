package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import org.opentest4j.MultipleFailuresError
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.internal.opentest4j.MappingFailed

internal object MapAssertions : JUnit5Minutests {
  fun tests() =
    rootContext<Assertion.Builder<Map<String, String>>> {
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

        test("withValue throws an exception") {
          assertThrows<MappingFailed> {
            withValue("foo") {
              isNotBlank()
            }
          }
        }

        test("keys mapping returns an empty set subject") {
          keys.isEmpty()
        }

        test("values mapping returns an empty collection subject") {
          values.isEmpty()
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
            val error =
              assertThrows<AssertionError> {
                containsKey("bar")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
              |  ✗ has an entry with the key "bar""""
                .trimMargin()
            )
          }
        }

        context("doesNotContainKey assertion") {
          test("passes if the subject doesn't have a matching key") {
            doesNotContainKey("bar")
          }

          test("fails if the subject does have a matching key") {
            val error =
              assertThrows<AssertionError> {
                doesNotContainKey("foo")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
              |  ✗ does not have an entry with the key "foo""""
                .trimMargin()
            )
          }
        }

        context("containsKeys assertion") {
          test("passes if the subject has all the specified keys") {
            containsKeys("foo", "baz")
          }

          test("fails if the subject does not have a matching key") {
            val error =
              assertThrows<AssertionError> {
                containsKeys("foo", "bar", "fnord")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
              |  ✗ has entries with the keys ["foo", "bar", "fnord"]
              |    ✓ has an entry with the key "foo"
              |    ✗ has an entry with the key "bar"
              |    ✗ has an entry with the key "fnord""""
                .trimMargin()
            )
          }
        }

        context("doesNotContainKeys assertion") {
          test("passes if the subject does not have all the specified keys") {
            doesNotContainKeys("bar", "fnord")
          }

          test("fails if the subject does have a matching key") {
            val error =
              assertThrows<AssertionError> {
                doesNotContainKeys("bar", "fnord", "foo")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
              |  ✗ doesn't have entries with the keys ["bar", "fnord", "foo"]
              |    ✓ does not have an entry with the key "bar"
              |    ✓ does not have an entry with the key "fnord"
              |    ✗ does not have an entry with the key "foo""""
                .trimMargin()
            )
          }
        }

        context("hasEntry assertion") {
          test("passes if the subject has a matching key value pair") {
            hasEntry("foo", "bar")
          }

          test("fails if the subject does not have a matching key") {
            val error =
              assertThrows<AssertionError> {
                hasEntry("bar", "foo")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
              |  ✗ has an entry with the key "bar""""
                .trimMargin()
            )
          }

          test("fails if the subject has a different value associated with the key") {
            val error =
              assertThrows<AssertionError> {
                hasEntry("foo", "baz")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that {"foo"="bar", "baz"="fnord", "qux"="fnord"}:
              |  ✓ has an entry with the key "foo"
              |  ▼ entry ["foo"]:
              |    ✗ is equal to "baz"
              |            found "bar""""
                .trimMargin()
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

        context("withValue function") {
          test("runs assertions on the value associated with the key") {
            withValue("foo") {
              isEqualTo("bar")
            }
          }

          test("fails if the nested assertions fail") {
            assertThrows<MultipleFailuresError> {
              withValue("foo") {
                isEqualTo("baz")
              }
            }
          }

          test("fails for a non-existent key") {
            assertThrows<MappingFailed> {
              withValue("bar") {
                isEqualTo("this will never get evaluated")
              }
            }.also {
              expectThat(it.message)
                .isEqualTo("Mapping 'entry [\"bar\"]' failed with: Key bar is missing in the map.")
            }
          }
        }

        context("keys mapping") {
          test("returns the map keys as a subject") {
            keys.isEqualTo(setOf("foo", "baz", "qux"))
          }
        }

        context("values mapping") {
          test("returns the map values as a subject") {
            values.containsExactlyInAnyOrder("bar", "fnord", "fnord")
          }
        }
      }
    }
}
