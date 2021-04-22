package strikt.arrow

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("assertions on Option")
object OptionAssertions : JUnit5Minutests {

  fun tests() = rootContext<Assertion.Builder<Option<String>>> {
    context("someOption") {
      fixture {
        expectThat(Some("aValue"))
      }

      test("can assert on type") {
        isSome()
      }

      test("can negate assertion") {
        not().isNone()
      }

      test("can assert on type and value equality") {
        isSome("aValue")
      }

      test("can assert on type and traverse wrapped value") {
        isSome().value.isEqualTo("aValue")
      }
    }

    context("noneOption") {
      fixture {
        expectThat(None)
      }

      test("can assert on type") {
        isNone()
      }

      test("can negate assertion") {
        not().isSome()
      }
    }
  }
}
