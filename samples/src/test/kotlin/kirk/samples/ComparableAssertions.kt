package kirk.samples

import kirk.api.AssertionFailed
import kirk.api.expect
import kirk.assertions.isGreaterThan
import kirk.assertions.isGreaterThanOrEqualTo
import kirk.assertions.isLessThan
import kirk.assertions.isLessThanOrEqualTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate

internal object ComparableAssertions {

  @Test
  fun isGreaterThan() {
    expect(1).isGreaterThan(0)
  }

  @Test
  fun isGreaterThanFailsEqualValue() {
    assertThrows<AssertionFailed> {
      expect(1).isGreaterThan(1)
    }
  }

  @Test
  fun isGreaterThanFailsLesserValue() {
    assertThrows<AssertionFailed> {
      expect(LocalDate.of(2018, 5, 1)).isGreaterThan(LocalDate.of(2018, 5, 2))
    }
  }

  @DisplayName("isLessThan passes if the subject is less than or equal to the expected value")
  @ParameterizedTest(name = "assertion passes: {0} < {1}")
  @MethodSource("isLessThanPassSource")
  fun <T : Comparable<T>> isLessThan(subject: T, expected: T) {
    expect(subject).isLessThan(expected)
  }

  @Suppress("unused")
  @JvmStatic
  fun isLessThanPassSource(): List<Arguments> = listOf(
    Arguments.of(0, 1),
    Arguments.of(1, 2),
    Arguments.of("catflap", "rubberplant"),
    Arguments.of(LocalDate.of(2018, 5, 8), LocalDate.of(2018, 5, 9))
  )

  @DisplayName("isLessThan fails if the subject is greater than or equal to the expected value")
  @ParameterizedTest(name = "assertion fails: {0} < {1}")
  @MethodSource("isLessThanFailSource")
  fun <T : Comparable<T>> isLessThanFails(subject: T, expected: T) {
    assertThrows<AssertionFailed> {
      expect(subject).isLessThan(expected)
    }
  }

  @Suppress("unused")
  @JvmStatic
  fun isLessThanFailSource(): List<Arguments> = listOf(
    Arguments.of(1, 1),
    Arguments.of(1, 0),
    Arguments.of("catflap", "catflap"),
    Arguments.of("rubberplant", "catflap"),
    Arguments.of(LocalDate.of(2018, 5, 8), LocalDate.of(2018, 5, 8)),
    Arguments.of(LocalDate.of(2018, 6, 1), LocalDate.of(2018, 5, 8))
  )

  @Test
  fun isGreaterThanOrEqualTo() {
    expect(1).isGreaterThanOrEqualTo(0)
  }

  @Test
  fun isGreaterThanOrEqualToEqualValue() {
    expect(1).isGreaterThanOrEqualTo(1)
  }

  @Test
  fun isGreaterThanOrEqualToLesserValue() {
    assertThrows<AssertionFailed> {
      expect(LocalDate.of(2018, 5, 1)).isGreaterThanOrEqualTo(LocalDate.of(2018, 5, 2))
    }
  }

  @Test
  fun isLessThanOrEqualTo() {
    expect(0).isLessThanOrEqualTo(1)
  }

  @Test
  fun isLessThanOrEqualToEqualValue() {
    expect(1).isLessThanOrEqualTo(1)
  }

  @Test
  fun isLessThanOrEqualToGreaterValue() {
    assertThrows<AssertionFailed> {
      expect(LocalDate.of(2018, 5, 2)).isLessThanOrEqualTo(LocalDate.of(2018, 5, 1))
    }
  }
}
