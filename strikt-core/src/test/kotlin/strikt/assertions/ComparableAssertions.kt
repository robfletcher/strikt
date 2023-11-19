package strikt.assertions

import dev.minutest.TestContextBuilder
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.Assertion
import strikt.api.expectThat
import java.time.Instant

@DisplayName("assertions on Comparable")
internal object ComparableAssertions {
  fun <T : Comparable<T>> TestContextBuilder<*, Assertion.Builder<T>>.supportsComparisonAssertions(
    value: T,
    decrementor: T.() -> T,
    incrementor: T.() -> T
  ) {
    fixture { expectThat(value) }

    context("isGreaterThan assertion") {
      test("passes if the subject is greater than the expected value") {
        isGreaterThan(value.decrementor())
      }

      test("fails if the subject is equal to the expected value") {
        assertThrows<AssertionError> {
          isGreaterThan(value)
        }
      }

      test("fails if the subject is less than the expected value") {
        assertThrows<AssertionError> {
          isGreaterThan(value.incrementor())
        }
      }
    }

    context("isGreaterThanOrEqualTo assertion") {
      test("passes if the subject is greater than the expected value") {
        isGreaterThanOrEqualTo(value.decrementor())
      }

      test("passes if the subject is equal to the expected value") {
        isGreaterThanOrEqualTo(value)
      }

      test("fails if the subject is less than the expected value") {
        assertThrows<AssertionError> {
          isGreaterThanOrEqualTo(value.incrementor())
        }
      }
    }

    context("isLessThan assertion") {
      test("fails if the subject is greater than the expected value") {
        assertThrows<AssertionError> {
          isLessThan(value.decrementor())
        }
      }

      test("fails if the subject is equal to the expected value") {
        assertThrows<AssertionError> {
          isLessThan(value)
        }
      }

      test("passes if the subject is less than the expected value") {
        isLessThan(value.incrementor())
      }
    }

    context("isLessThanOrEqualTo assertion") {
      test("fails if the subject is greater than the expected value") {
        assertThrows<AssertionError> {
          isLessThanOrEqualTo(value.decrementor())
        }
      }

      test("passes if the subject is equal to the expected value") {
        isLessThanOrEqualTo(value)
      }

      test("passes if the subject is less than the expected value") {
        isLessThanOrEqualTo(value.incrementor())
      }
    }

    context("comparesEqualTo assertion") {
      test("fails if the subject is greater than the expected value") {
        assertThrows<AssertionError> {
          comparesEqualTo(value.decrementor())
        }
      }

      test("fails if the subject is less than the expected value") {
        assertThrows<AssertionError> {
          comparesEqualTo(value.incrementor())
        }
      }

      test("passes if the subject is equal to the expected value") {
        comparesEqualTo(value)
      }
    }
  }

  @TestFactory
  fun comparableAssertions_Int() =
    assertionTests<Int> {
      context("an Int subject") {
        supportsComparisonAssertions(1, Int::dec, Int::inc)
      }
    }

  @TestFactory
  fun comparableAssertions_Instant() =
    assertionTests<Instant> {
      context("an Instant subject") {
        supportsComparisonAssertions(
          Instant.now(),
          { minusMillis(1) },
          { plusMillis(1) }
        )
      }
    }

  @TestFactory
  fun comparableAssertions_String() =
    assertionTests<String> {
      context("a String subject") {
        supportsComparisonAssertions("a", { "A" }, { "z" })
      }
    }

  @TestFactory
  fun isIn_Int() =
    assertionTests<Int> {
      val range = 1..10
      range.forEach { i ->
        context("a value of $i") {
          fixture { expectThat(i) }

          test("is in the range ${range.start}..${range.endInclusive}") {
            expectThat(i).isIn(range)
          }
        }
      }

      ((-5..0) + (11..15)).forEach { i ->
        context("a value of $i") {
          fixture { expectThat(i) }

          test("$i is not in the range ${range.start}..${range.endInclusive}") {
            assertThrows<AssertionError> {
              expectThat(i).isIn(range)
            }
          }
        }
      }
    }

  @TestFactory
  fun isIn_Long() =
    assertionTests<Long> {
      val range = 1L..10L
      range.forEach { i ->
        context("a value of $i") {
          fixture { expectThat(i) }

          test("is in the range ${range.start}..${range.endInclusive}") {
            expectThat(i).isIn(range)
          }
        }
      }

      ((-5L..0L) + (11L..15L)).forEach { i ->
        context("a value of $i") {
          fixture { expectThat(i) }

          test("$i is not in the range ${range.start}..${range.endInclusive}") {
            assertThrows<AssertionError> {
              expectThat(i).isIn(range)
            }
          }
        }
      }
    }
}
