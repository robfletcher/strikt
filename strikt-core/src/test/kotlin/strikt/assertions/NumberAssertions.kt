package strikt.assertions

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import strikt.api.expect
import strikt.fails

internal class NumberAssertions {

  @TestFactory
  fun `can compare two doubles with a tolerance`() =
    listOf(
      Triple(5.0, 5.001, 0.1),
      Triple(5.0, 5.0001, 0.001)
    ).map { (a, b, tolerance) ->
      dynamicTest("$a is within $tolerance of $b") {
        expect(a).isEqualTo(b, tolerance)
      }
    }

  @Test
  fun `isEqualTo within tolerance works with floats`() {
    expect(5.0f).isEqualTo(5.001f, 0.1)
  }

  @TestFactory
  fun `fails if expected value is outside of tolerance`() =
    listOf(
      Triple(5.0, 5.11, 0.1),
      Triple(5.0, 6.0, 0.999),
      Triple(5.0, 5.10000001, 0.1)
    ).map { (a, b, tolerance) ->
      dynamicTest("$a is not within $tolerance of $b") {
        fails {
          expect(a).isEqualTo(b, tolerance)
        }
      }
    }
}
