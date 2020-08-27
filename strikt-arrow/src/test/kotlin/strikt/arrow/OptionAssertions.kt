package strikt.arrow

import arrow.core.Option
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("assertions on Option")
object OptionAssertions : JUnit5Minutests {

  fun tests() = rootContext {
    context("someOption") {
      val option = Option.just("aValue")

      test("can assert on type") {
        expectThat(option).isSome()
      }

      test("can negate assertion") {
        expectThat(option).not().isNone()
      }

      test("can assert on type and value equality") {
        expectThat(option).isSome("aValue")
      }

      test("can assert on type and traverse wrapped value") {
        expectThat(option).isSome().t.isEqualTo("aValue")
      }
    }

    context("noneOption") {
      val option = Option.empty<String>()

      test("can assert on type") {
        expectThat(option).isNone()
      }

      test("can negate assertion") {
        expectThat(option).not().isSome()
      }
    }
  }
}
