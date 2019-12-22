package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import java.util.Optional
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.expectThat

internal object OptionalAssertions : JUnit5Minutests {
  fun isPresentTests() = rootContext<Assertion.Builder<Optional<String>>> {
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

  fun isAbsentTests() = rootContext<Assertion.Builder<Optional<String>>> {
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

  fun toNullableTests() = rootContext<Assertion.Builder<Optional<String>>> {
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
