package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion.Builder
import strikt.api.expectThat
import java.time.Instant

internal object AnyAssertions : JUnit5Minutests {

  fun tests() = rootContext<Builder<Any?>> {
    context("isNull") {
      context("the subject is null") {
        fixture { expectThat(null) }

        test("the assertion passes") {
          isNull()
        }

        test("the assertion down-casts the subject") {
          @Suppress("USELESS_IS_CHECK")
          also { assert(it is Builder<Any?>) }
            .isNull()
            .also { assert(it is Builder<Nothing>) }
        }
      }

      listOf("fnord", 1L, "null").forEach {
        context("a non-null subject : ${it.quoted()}") {
          fixture { expectThat(it) }

          test("the assertion fails") {
            assertThrows<AssertionFailedError> {
              isNull()
            }
          }
        }
      }
    }

    context("isNotNull") {
      context("when the subject is null") {
        fixture { expectThat(null) }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isNotNull()
          }
        }
      }

      listOf("fnord", 1L, "null").forEach<Any?> {
        context("a non-null subject : ${it.quoted()}") {
          fixture { expectThat(it) }

          test("the assertion passes") {
            isNotNull()
          }

          test("down-casts the result") {
            @Suppress("USELESS_IS_CHECK")
            also { assert(it is Builder<Any?>) }
              .isNotNull()
              .also { assert(it is Builder<Any>) }
          }
        }
      }
    }

    context("withNotNull") {
      context("when the subject is null") {
        fixture { expectThat(null) }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            withNotNull {
              isEqualTo("fnord")
            }
          }
        }
      }

      listOf("fnord", 1L, "null").forEach<Any?> {
        context("a non-null subject : ${it.quoted()}") {
          fixture { expectThat(it) }

          test("the assertion passes") {
            withNotNull {
              isEqualTo(it)
            }
          }
        }
      }
    }

    context("isA") {
      context("when the subject is null") {
        fixture { expectThat(null) }
        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isA<String>()
          }
        }
      }

      context("a subject of the wrong type") {
        fixture { expectThat(1L) }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isA<String>()
          }
        }
      }

      context("a subject of the expected type") {
        fixture { expectThat("fnord") }

        test("the assertion passes") {
          isA<String>()
        }

        test("the assertion narrows the subject type") {
          @Suppress("USELESS_IS_CHECK")
          also { assert(it is Builder<Any?>) }
            .isA<String>()
            .also { assert(it is Builder<String>) }
        }

        test("the narrowed type can use specialized assertions") {
          isA<String>().hasLength(5) // only available on Assertion<CharSequence>
        }
      }

      context("a subject that is a sub-type of the expected type") {
        fixture { expectThat(1L) }

        test("the assertion passes") {
          isA<Number>()
        }

        test("the assertion narrows the subject type") {
          @Suppress("USELESS_IS_CHECK")
          also { assert(it is Builder<Any?>) }
            .isA<Number>()
            .also { assert(it is Builder<Number>) }
            .isA<Long>()
            .also { assert(it is Builder<Long>) }
        }
      }
    }

    context("isEqualTo") {
      context("a subject that is equal to the expectation") {
        fixture { expectThat("fnord") }

        test("the assertion passes") {
          isEqualTo("fnord")
        }
      }

      listOf(
        "fnord" to "FNORD",
        1 to 1L,
        null to "fnord",
        "fnord" to null
      )
        .forEach { (subject, expected) ->
          context("when the subject is ${subject.quoted()}") {
            fixture { expectThat(subject) }

            test("the assertion fails") {
              assertThrows<AssertionFailedError> {
                isEqualTo(expected)
              }
            }
          }
        }

      context("subject is a different type but looks the same") {
        fixture { expectThat(5L) }

        test("the failure message specifies the types involved") {
          val error = assertThrows<AssertionFailedError> {
            isEqualTo(5)
          }
          expectThat(error.message).isEqualTo(
            """▼ Expect that 5:
            |  ✗ is equal to 5 (Int)
            |          found 5 (Long)"""
              .trimMargin().replace("\n", System.lineSeparator())
          )
        }
      }
    }

    context("isNotEqualTo") {
      context("the subject matches the expectation") {
        fixture { expectThat("fnord") }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isNotEqualTo("fnord")
          }
        }
      }

      listOf(
        "fnord" to "FNORD",
        1 to 1L,
        null to "fnord",
        "fnord" to null
      )
        .forEach { (subject, expected) ->
          context("when the subject is ${subject.quoted()} and the expected value is ${expected.quoted()}") {
            fixture { expectThat(subject) }

            test("the assertion passes") {
              isNotEqualTo(expected)
            }
          }
        }
    }

    context("isSameInstanceAs") {
      listOf(
        listOf("fnord") to listOf("fnord"),
        null to listOf("fnord"),
        listOf("fnord") to null,
        1 to 1L,
        Instant.now().let { it to Instant.ofEpochMilli(it.toEpochMilli()) }
      )
        .forEach { (subject, expected) ->
          context("the subject and expected values are different instances") {
            fixture { expectThat(subject) }

            test("the assertion fails") {
              assertThrows<AssertionFailedError> {
                isSameInstanceAs(expected)
              }
            }
          }
        }

      listOf("fnord", 1L, null, listOf("fnord"), Instant.now())
        .map { it to it }
        .forEach { (subject, expected) ->
          context("the subject and expected values are the same instance") {
            fixture { expectThat(subject) }

            test("the assertion passes") {
              isSameInstanceAs(expected)
            }
          }
        }
    }

    context("isNotSameInstanceAs") {
      listOf(
        listOf("fnord") to listOf("fnord"),
        null to listOf("fnord"),
        listOf("fnord") to null,
        1 to 1L,
        Instant.now().let { it to Instant.ofEpochMilli(it.toEpochMilli()) }
      )
        .forEach { (subject, expected) ->
          context("the subject and expected values are different instances") {
            fixture { expectThat(subject) }

            test("the assertion passes") {
              isNotSameInstanceAs(expected)
            }
          }
        }

      listOf("fnord", 1L, null, listOf("fnord"), Instant.ofEpochMilli(0))
        .map { it to it }
        .forEach { (subject, expected) ->
          context("the subject and expected values are the same instance") {
            fixture { expectThat(subject) }

            test("the assertion fails") {
              assertThrows<AssertionFailedError> {
                isNotSameInstanceAs(expected)
              }
            }
          }
        }
    }

    context("isContainedIn") {
      context("with a collection of the same type") {
        fixture {
          expectThat("fnord")
        }

        test("fails if the subject is not in the expected iterable") {
          assertThrows<AssertionFailedError> {
            isContainedIn(listOf("catflap", "rubberplant", "marzipan"))
          }
        }

        test("passes if the subject is in the expected iterable") {
          isContainedIn(listOf("catflap", "rubberplant", "marzipan", "fnord"))
        }
      }

      context("with a collection of a super type") {
        fixture {
          expectThat(1)
        }

        test("passes if the subject is in the expected iterable") {
          // this is really just testing compilation works
          isContainedIn(listOf<Number>(1, 1L, 1.0, 1.0f))
        }
      }
    }
  }
}
