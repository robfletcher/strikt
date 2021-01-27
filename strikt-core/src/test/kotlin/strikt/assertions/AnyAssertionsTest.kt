package strikt.assertions

import failfast.describe
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion.Builder
import strikt.api.expectThat
import java.time.Instant

internal object AnyAssertionsTest {

  val context = describe("assertions on Any?") {
    context("isNull") {
      context("the subject is null") {
        val assertion: Builder<Any?> = expectThat(null)

        test("the assertion passes") {
          assertion.isNull()
        }

        test("the assertion down-casts the subject") {
          @Suppress("USELESS_IS_CHECK")
          assertion.also { assert(it is Builder<Any?>) }
            .isNull()
            .also { assert(it is Builder<Nothing>) }
        }
      }

      listOf("fnord", 1L, "null").forEach {
        context("a non-null subject : ${it.quoted()}") {
          val assertion: Builder<Any?> = expectThat(it)

          test("the assertion fails") {
            assertThrows<AssertionFailedError> {
              assertion.isNull()
            }
          }
        }
      }
    }

    context("isNotNull") {
      context("when the subject is null") {
        val assertion: Builder<Any?> = expectThat(null)

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            assertion.isNotNull()
          }
        }
      }

      listOf("fnord", 1L, "null").forEach<Any?> {
        context("a non-null subject : ${it.quoted()}") {
          val assertion: Builder<Any?> = expectThat(it)

          test("the assertion passes") {
            assertion.isNotNull()
          }

          test("down-casts the result") {
            @Suppress("USELESS_IS_CHECK")
            assertion.also { assert(it is Builder<Any?>) }
              .isNotNull()
              .also { assert(it is Builder<Any>) }
          }
        }
      }
    }

    context("isA") {
      context("when the subject is null") {
        val assertion: Builder<Any?> = expectThat(null)

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            assertion.isA<String>()
          }
        }
      }

      context("a subject of the wrong type") {
        val assertion: Builder<Any?> = expectThat(1L)

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            assertion.isA<String>()
          }
        }
      }

      context("a subject of the expected type") {
        val assertion: Builder<Any?> = expectThat("fnord")

        test("the assertion passes") {
          assertion.isA<String>()
        }

        test("the assertion narrows the subject type") {
          @Suppress("USELESS_IS_CHECK")
          assertion.also { assert(it is Builder<Any?>) }
            .isA<String>()
            .also { assert(it is Builder<String>) }
        }

        test("the narrowed type can use specialized assertions") {
          assertion.isA<String>().hasLength(5) // only available on Assertion<CharSequence>
        }
      }

      context("a subject that is a sub-type of the expected type") {
        val assertion: Builder<Any?> = expectThat(1L)

        test("the assertion passes") {
          assertion.isA<Number>()
        }

        test("the assertion narrows the subject type") {
          @Suppress("USELESS_IS_CHECK")
          assertion.also { assert(it is Builder<Any?>) }
            .isA<Number>()
            .also { assert(it is Builder<Number>) }
            .isA<Long>()
            .also { assert(it is Builder<Long>) }
        }
      }
    }

    context("isEqualTo") {
      context("a subject that is equal to the expectation") {
        val assertion: Builder<Any?> = expectThat("fnord")

        test("the assertion passes") {
          assertion.isEqualTo("fnord")
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
            val assertion: Builder<Any?> = expectThat(subject)

            test("the assertion fails") {
              assertThrows<AssertionFailedError> {
                assertion.isEqualTo(expected)
              }
            }
          }
        }

      context("subject is a different type but looks the same") {
        val assertion: Builder<Any?> = expectThat(5L)

        test("the failure message specifies the types involved") {
          val error = assertThrows<AssertionFailedError> {
            assertion.isEqualTo(5)
          }
          expectThat(error.message).isEqualTo(
            """▼ Expect that 5:
            |  ✗ is equal to 5 (Int)
            |          found 5 (Long)""".trimMargin()
          )
        }
      }
    }

    context("isNotEqualTo") {
      context("the subject matches the expectation") {
        val assertion: Builder<Any?> = expectThat("fnord")

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            assertion.isNotEqualTo("fnord")
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
            val assertion: Builder<Any?> = expectThat(subject)

            test("the assertion passes") {
              assertion.isNotEqualTo(expected)
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
          context("the subject and expected values are different instances: ${subject?.javaClass?.simpleName} vs ${expected?.javaClass?.simpleName}") {
            val assertion: Builder<Any?> = expectThat(subject)

            test("the assertion fails") {
              assertThrows<AssertionFailedError> {
                assertion.isSameInstanceAs(expected)
              }
            }
          }
        }

      listOf("fnord", 1L, null, listOf("fnord"), Instant.now())
        .map { it to it }
        .forEach { (subject, expected) ->
          context("the subject and expected values are the same instance: ${subject?.javaClass?.simpleName} vs ${expected?.javaClass?.simpleName}") {
            val assertion: Builder<Any?> = expectThat(subject)

            test("the assertion passes") {
              assertion.isSameInstanceAs(expected)
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
          context("the subject and expected values are different instances: ${subject?.javaClass?.simpleName} vs ${expected?.javaClass?.simpleName}") {
            val assertion: Builder<Any?> = expectThat(subject)

            test("the assertion passes") {
              assertion.isNotSameInstanceAs(expected)
            }
          }
        }

      listOf("fnord", 1L, null, listOf("fnord"), Instant.ofEpochMilli(0))
        .map { it to it }
        .forEach { (subject, expected) ->
          context("the subject and expected values are the same instance: ${subject?.javaClass?.simpleName} vs ${expected?.javaClass?.simpleName}") {
            val assertion: Builder<Any?> = expectThat(subject)

            test("the assertion fails") {
              assertThrows<AssertionFailedError> {
                assertion.isNotSameInstanceAs(expected)
              }
            }
          }
        }
    }
  }
}
