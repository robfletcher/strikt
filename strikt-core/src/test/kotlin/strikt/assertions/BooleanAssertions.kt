package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

@Suppress("SimplifyBooleanWithConstants")
internal object BooleanAssertions : JUnit5Minutests {
  fun tests() =
    rootContext {
      context("isTrue") {
        test("passes when the subject is true") {
          expectThat("a" == "a").isTrue()
        }

        test("fails when the subject is false") {
          assertThrows<AssertionError> {
            expectThat("a" == "A").isTrue()
          }
        }

        test("fails when the subject is null") {
          assertThrows<AssertionError> {
            expectThat(null).isTrue()
          }
        }
      }

      context("isFalse") {
        test("passes when the subject is false") {
          expectThat("a" == "A").isFalse()
        }

        test("fails when the subject is false") {
          assertThrows<AssertionError> {
            expectThat("a" == "a").isFalse()
          }
        }

        test("fails when the subject is null") {
          assertThrows<AssertionError> {
            expectThat(null).isFalse()
          }
        }
      }
    }
}
