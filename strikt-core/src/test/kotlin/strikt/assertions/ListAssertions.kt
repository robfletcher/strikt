package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

@DisplayName("assertions on List")
internal object ListAssertions {


  @TestFactory
  @DisplayName("containsSequence assertion")
  fun containsSequence() = assertionTests<List<Any?>> {
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
          assertEquals(
            """▼ Expect that [1, 2, 3, 4]:
            |  ✗ contains the sequence: [1, 4] in exactly the same order""".trimMargin(),
            exception.message
          )
        }

        test("the expected sequence is longer than the subject") {
          val exception = assertThrows<AssertionError> {
            expectThat(fixture)
              .containsSequence(1, 2, 3, 4, 5)
          }
          assertEquals(
            """▼ Expect that [1, 2, 3, 4]:
            |  ✗ contains the sequence: [1, 2, 3, 4, 5] in exactly the same order : expected sequence cannot be longer than subject""".trimMargin(),
            exception.message
          )
        }

        test("the expected sequence is empty") {
          val exception = assertThrows<AssertionError> {
            expectThat(fixture)
              .containsSequence()
          }
          assertEquals(
            """▼ Expect that [1, 2, 3, 4]:
            |  ✗ contains the sequence: [] in exactly the same order : expected sequence cannot empty""".trimMargin(),
            exception.message
          )
        }
      }
    }
  }
}
