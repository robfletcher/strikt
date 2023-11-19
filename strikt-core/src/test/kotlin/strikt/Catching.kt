package strikt

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure
import strikt.assertions.isSuccess
import strikt.assertions.message

internal class Catching : JUnit5Minutests {
  fun tests() =
    rootContext<Assertion.Builder<Result<String>>> {
      context("a successful action") {
        fixture {
          expectCatching { "kthxbye" }
        }

        test("maps to the action's returned value") {
          isSuccess()
            .isA<String>()
            .isEqualTo("kthxbye")
        }

        test("is not failed") {
          assertThrows<AssertionFailedError> {
            isFailure()
          }
        }

        test("chains correctly in a block") {
          assertThrows<AssertionError> {
            and {
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
        fixture {
          expectCatching { error("o noes") }
        }

        test("maps to the exception thrown by the action") {
          isFailure()
            .isA<IllegalStateException>()
            .message
            .isEqualTo("o noes")
        }

        test("is not successful") {
          assertThrows<AssertionFailedError> {
            isSuccess()
          }
        }

        test("chains correctly in a block") {
          assertThrows<AssertionError> {
            and {
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
