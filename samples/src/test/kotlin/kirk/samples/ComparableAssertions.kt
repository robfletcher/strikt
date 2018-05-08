package kirk.samples

import kirk.api.AssertionFailed
import kirk.api.expect
import kirk.assertions.isGreaterThan
import kirk.assertions.isGreaterThanOrEqualTo
import kirk.assertions.isLessThan
import kirk.assertions.isLessThanOrEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

  @Test
  fun isLessThan() {
    expect(0).isLessThan(1)
  }

  @Test
  fun isLessThanEqualValue() {
    assertThrows<AssertionFailed> {
      expect(1).isLessThan(1)
    }
  }

  @Test
  fun isLessThanGreaterValue() {
    assertThrows<AssertionFailed> {
      expect(LocalDate.of(2018, 5, 2)).isLessThan(LocalDate.of(2018, 5, 1))
    }
  }

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
