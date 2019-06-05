package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

@DisplayName("assertions on Number")
internal object NumberAssertions {

  @TestFactory
  fun `can compare two doubles with a tolerance`() = testFactory<Unit> {
    listOf(
      Triple(5.0, 5.001, 0.1),
      Triple(5.0, 5.0001, 0.001)
    ).forEach { (a, b, tolerance) ->
      test("$a is within $tolerance of $b") {
        expectThat(a).isEqualTo(b, tolerance)
      }
    }
  }

  @Test
  fun `isEqualTo within tolerance works with floats`() {
    expectThat(5.0f).isEqualTo(5.001f, 0.1)
  }

  @TestFactory
  fun `fails if expected value is outside of tolerance`() = testFactory<Unit> {
    listOf(
      Triple(5.0, 5.11, 0.1),
      Triple(5.0, 6.0, 0.999),
      Triple(5.0, 5.10000001, 0.1)
    ).forEach { (a, b, tolerance) ->
      test("$a is not within $tolerance of $b") {
        assertThrows<AssertionError> {
          expectThat(a).isEqualTo(b, tolerance)
        }
      }
    }
  }
}
