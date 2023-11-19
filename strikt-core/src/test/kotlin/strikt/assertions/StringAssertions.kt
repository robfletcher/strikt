package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.internal.opentest4j.CompoundAssertionFailure

@DisplayName("assertions on String")
internal object StringAssertions : JUnit5Minutests {
  fun tests() =
    rootContext {
      derivedContext<Assertion.Builder<String>>("isEqualToIgnoringCase") {
        fixture { expectThat("fnord") }

        test("passes if the subject is identical to the expected value") {
          isEqualToIgnoringCase("fnord")
        }

        test("fails if the subject is different") {
          assertThrows<AssertionError> {
            expectThat("despite the negative press fnord")
              .isEqualToIgnoringCase("fnord")
          }
        }

        test("passes if the subject is the same as the expected value apart from case") {
          expectThat("fnord").isEqualToIgnoringCase("fnord")
        }
      }

      derivedContext<Assertion.Builder<String>>("startsWith") {
        fixture { expectThat("fnord") }

        test("can expect string start") {
          expectThat("fnord").startsWith("fno")
        }

        test("outputs real start when startsWith fails") {
          val expectThrows =
            expectThrows<AssertionError> {
              try {
                expectThat("fnord").startsWith("fnrd")
              } catch (e: Throwable) {
                throw e
              }
            }
          expectThrows
            .message
            .isNotNull()
            .contains(
              """▼ Expect that "fnord":
                    |  ✗ starts with "fnrd"
                    |          found "fnor"
              """.trimMargin()
            )
        }
      }

      derivedContext<Assertion.Builder<String>>("endsWith") {
        fixture { expectThat("fnord") }

        test("can expect string end") {
          expectThat("fnord").endsWith("nord")
        }

        test("outputs real end when endsWith fails") {
          expectThrows<AssertionError> {
            expectThat("fnord").endsWith("nor")
          }
            .message
            .isNotNull()
            .isEqualTo(
              """▼ Expect that "fnord":
                     |  ✗ ends with "nor"
                     |        found "ord"
              """.trimMargin()
            )
        }
      }

      test("can have a block assertion on a string subject without overload clash") {
        val error =
          assertThrows<CompoundAssertionFailure> {
            val subject = "The Enlightened take things Lightly"
            expectThat(subject = subject) {
              hasLength(5)
              matches(Regex("\\d+"))
              startsWith("T")
            }
          }
        expectThat(error.failures.size).isEqualTo(2)
      }

      test("can trim string") {
        expectThat(" fnord ").trim().isEqualToIgnoringCase("fnord")
      }
    }
}
