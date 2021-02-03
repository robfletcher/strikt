package strikt.java

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import java.util.Optional

internal object OptionalAssertions : JUnit5Minutests {
  fun tests() = rootContext<Assertion.Builder<Optional<String>>> {
    context("isPresent") {
      context("the optional subject contains a value") {
        fixture { expectThat(Optional.of("fnord")) }

        test("the assertion passes") {
          isPresent()
        }
      }

      context("the optional subject is empty") {
        fixture { expectThat(Optional.empty()) }

        test("the assertion passes") {
          assertThrows<AssertionFailedError> {
            isPresent()
          }
        }
      }
    }

    context("isAbsent") {
      context("the optional subject contains a value") {
        fixture { expectThat(Optional.of("fnord")) }

        test("the assertion passes") {
          assertThrows<AssertionFailedError> {
            isAbsent()
          }
        }
      }

      context("the optional subject is empty") {
        fixture { expectThat(Optional.empty()) }

        test("the assertion passes") {
          isAbsent()
        }
      }
    }

    context("toNullable") {
      context("the optional subject contains a value") {
        fixture { expectThat(Optional.of("fnord")) }

        test("the subject is transformed to the optional value") {
          toNullable().isNotNull().isEqualTo("fnord")
        }
      }

      context("the optional subject is empty") {
        fixture { expectThat(Optional.empty()) }

        test("the subject is transformed to null") {
          toNullable().isNull()
        }
      }
    }
  }
}
