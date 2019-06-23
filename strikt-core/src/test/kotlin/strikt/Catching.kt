package strikt

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.Try
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.failed
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.message
import strikt.assertions.succeeded

internal class Catching : JUnit5Minutests {
  fun tests() = rootContext<Assertion.Builder<Try<String>>> {
    context("a successful action") {
      fixture {
        expectCatching { "kthxbye" }
      }

      test("maps to the action's returned value") {
        succeeded()
          .isA<String>()
          .isEqualTo("kthxbye")
      }

      test("is not failed") {
        assertThrows<AssertionFailedError> {
          failed()
        }
      }

      test("chains correctly in a block") {
        assertThrows<AssertionError> {
          and {
            failed().isA<NullPointerException>()
          }
        }.also { exception ->
          expectThat(exception.message).isEqualTo(
            """
            |▼ Expect that Success(kthxbye):
            |  ✗ failed with an exception : returned "kthxbye"
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
        failed()
          .isA<IllegalStateException>()
          .message
          .isEqualTo("o noes")
      }

      test("is not successful") {
        assertThrows<AssertionFailedError> {
          succeeded()
        }
      }

      test("chains correctly in a block") {
        assertThrows<AssertionError> {
          and {
            succeeded().isA<String>()
          }
        }.also { exception ->
          expectThat(exception.message).isEqualTo(
            """
            |▼ Expect that Failure(IllegalStateException: o noes):
            |  ✗ succeeded : threw java.lang.IllegalStateException
          """.trimMargin()
          )
        }
      }
    }
  }
}
