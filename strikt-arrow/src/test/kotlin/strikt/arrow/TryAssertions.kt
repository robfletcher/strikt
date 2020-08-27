@file:Suppress("DEPRECATION")

package strikt.arrow

import arrow.core.Try
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.message

@DisplayName("assertions on Try")
object TryAssertions : JUnit5Minutests {

  fun tests() = rootContext {
    context("trySuccess") {
      val aTry = Try.just("success")

      test("can assert on type") {
        expectThat(aTry).isSuccess()
      }

      test("can negate assertion") {
        expectThat(aTry).not().isFailure()
      }

      test("can assert on type and value equality") {
        expectThat(aTry).isSuccess("success")
      }

      test("can assert on type and traverse value") {
        expectThat(aTry).isSuccess().value.isEqualTo("success")
      }
    }

    context("tryFailure") {
      val aTry = Try.raiseError(IllegalArgumentException("testFailure"))

      test("can assert on type") {
        expectThat(aTry).isFailure()
      }

      test("can negate assertion") {
        expectThat(aTry).not().isSuccess()
      }

      test("can assert on type and traverse exception") {
        expectThat(aTry).isFailure().exception.message.isEqualTo("testFailure")
      }
    }
  }
}
