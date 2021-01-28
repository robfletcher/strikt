package strikt

import failfast.describe
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure
import strikt.assertions.isSuccess
import strikt.assertions.message

internal object CatchingTest {
  val context = describe("Catching") {
    context("a successful action") {
      val fixture =
        expectCatching { "kthxbye" }

      test("maps to the action's returned value") {
        fixture.isSuccess()
          .isA<String>()
          .isEqualTo("kthxbye")
      }

      test("is not failed") {
        assertThrows<AssertionFailedError> {
          fixture.isFailure()
        }
      }

      test("chains correctly in a block") {
        assertThrows<AssertionError> {
          fixture.and {
            isFailure().isA<NullPointerException>()
          }
        }.also { exception ->
          expectThat(exception.message).isEqualTo(
            """
            |▼ Expect that Success(kthxbye):
            |  ✗ is failure
            |    returned "kthxbye"
          """.trimMargin()
          )
        }
      }
    }

    context("a failed action") {
      val fixture =
        expectCatching { error("o noes") }

      test("maps to the exception thrown by the action") {
        fixture.isFailure()
          .isA<IllegalStateException>()
          .message
          .isEqualTo("o noes")
      }

      test("is not successful") {
        assertThrows<AssertionFailedError> {
          fixture.isSuccess()
        }
      }

      test("chains correctly in a block") {
        assertThrows<AssertionError> {
          fixture.and {
            isSuccess().isA<String>()
          }
        }.also { exception ->
          expectThat(exception.message).isEqualTo(
            """
            |▼ Expect that Failure(java.lang.IllegalStateException: o noes):
            |  ✗ is success
            |    threw java.lang.IllegalStateException
          """.trimMargin()
          )
        }
      }
    }
  }
}
