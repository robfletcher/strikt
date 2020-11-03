package strikt

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.MultipleFailuresError
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isNotBlank

/**
 * @see https://github.com/robfletcher/strikt/issues/231
 */
@DisplayName("custom assertions")
class Custom {
  @Test
  fun `a failing simple assertion in a block fails appropriately`() {
    val result = 1 to "test"
    assertThrows<AssertionError> {
      expectThat(result) {
        isEqualTo(3 to "test")
      }
    }
  }

  @Test
  fun `a failing simple assertion fails appropriately when combined with a passing custom assertion in a block`() {
    val result = 1 to "test"
    expectThrows<MultipleFailuresError> {
      expectThat(result) {
        isEqualTo(3 to "test")
        hasLength(4)
      }
    }
      .get { failures }
      .hasSize(1)
  }

  @Test
  fun `a failing simple assertion fails appropriately when combined with a failing custom assertion in a block`() {
    val result = 1 to "test"
    expectThrows<MultipleFailuresError> {
      expectThat(result) {
        isEqualTo(3 to "test")
        hasLength(3)
      }
    }
      .get { failures }
      .hasSize(2)
  }

  private fun Assertion.Builder<Pair<Int, String>>.hasLength(expected: Int): Assertion.Builder<Pair<Int, String>> =
    compose("Has length equal to %s", expected) {
      get { second }
        .isNotBlank()
        .and {
          get { length }.isEqualTo(expected)
        }
    } then {
      if (allPassed) pass() else fail()
    }
}
