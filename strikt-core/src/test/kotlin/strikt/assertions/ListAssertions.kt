package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

internal object ListAssertions : JUnit5Minutests {

  fun tests() = rootContext {
    context("containsSequence assertion") {
      derivedContext<List<Int>>("a ${List::class}") {
        fixture { listOf(1, 2, 3, 4) }

        context("passes if") {
          test("the subject contains the sequence") {
            expectThat(fixture)
              .containsSequence(3, 4)
          }

          test("the subject contains exactly the same elements as the expected sequence") {
            expectThat(fixture)
              .containsSequence(1, 2, 3, 4)
          }
        }

        context("fails if") {
          test("the subject does not contain the sequence in the same order") {
            val exception = assertThrows<AssertionError> {
              expectThat(fixture)
                .containsSequence(1, 4)
            }
            expectThat(exception.message).isEqualTo(
              """▼ Expect that [1, 2, 3, 4]:
            |  ✗ contains the sequence: [1, 4] in exactly the same order"""
                  .trimMargin().replace("\n", System.lineSeparator())
            )
          }

          test("the expected sequence is longer than the subject") {
            val exception = assertThrows<AssertionError> {
              expectThat(fixture)
                .containsSequence(1, 2, 3, 4, 5)
            }
            expectThat(exception.message).isEqualTo(
              """▼ Expect that [1, 2, 3, 4]:
              |  ✗ contains the sequence: [1, 2, 3, 4, 5] in exactly the same order : expected sequence cannot be longer than subject"""
                  .trimMargin().replace("\n", System.lineSeparator())
            )
          }

          test("the expected sequence is empty") {
            val exception = assertThrows<AssertionError> {
              expectThat(fixture)
                .containsSequence()
            }
            expectThat(exception.message).isEqualTo(
              """▼ Expect that [1, 2, 3, 4]:
            |  ✗ contains the sequence: [] in exactly the same order : expected sequence cannot empty"""
                  .trimMargin().replace("\n", System.lineSeparator())
            )
          }
        }
      }
    }
  }
}
