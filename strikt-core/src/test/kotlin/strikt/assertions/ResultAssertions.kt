package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.expectThat

internal object ResultAssertions : JUnit5Minutests {
  fun tests() = rootContext<Assertion.Builder<Result<Int>>> {
    context("isFailure") {
      context("the result subject is a failure") {
        fixture { expectThat(Result.failure(Exception("boom"))) }

        test("the assertion passes") {
          isFailure().isA<Exception>().message.isEqualTo("boom")
        }
      }

      context("the result subject is a success") {
        fixture { expectThat(Result.success(42)) }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isFailure()
          }
        }
      }
    }

    context("isSuccess") {
      context("the result subject is a success") {
        fixture { expectThat(Result.success(42)) }

        test("the assertion passes") {
          isSuccess().isEqualTo(42)
        }
      }

      context("the result subject is a failure") {
        fixture { expectThat(Result.failure(Exception())) }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isSuccess()
          }
        }
      }
    }
  }
}
