package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.fails
import java.time.LocalDate

@DisplayName("assertions on Comparable")
internal class ComparableAssertions {
  @Nested
  @DisplayName("isGreaterThan assertion")
  inner class IsGreaterThan {
    @Test
    fun `passes if the subject is greater than the expected value`() {
      expect(1).isGreaterThan(0)
    }

    @Test
    fun `fails if the subject is equal to the expected value`() {
      fails {
        expect(1).isGreaterThan(1)
      }
    }

    @Test
    fun `fails if the subject is less than the expected value`() {
      fails {
        expect(LocalDate.of(2018, 5, 1)).isGreaterThan(LocalDate.of(2018, 5, 2))
      }
    }
  }

  @Nested
  @DisplayName("isLessThan assertion")
  inner class IsLessThan {
    @Test
    fun `passes if the subject is less than the expected value`() {
      expect(0).isLessThan(1)
    }

    @Test
    fun `fails if the subject is equal to the expected value`() {
      fails {
        expect(1).isLessThan(1)
      }
    }

    @Test
    fun `fails if the subject is greater than the expected value`() {
      fails {
        expect(LocalDate.of(2018, 5, 2)).isLessThan(LocalDate.of(2018, 5, 1))
      }
    }
  }

  @Nested
  @DisplayName("isGreaterThanOrEqualTo assertion")
  inner class IsGreaterThanOrEqualTo {
    @Test
    fun `passes if the subject is greater than the expected value`() {
      expect(1).isGreaterThanOrEqualTo(0)
    }

    @Test
    fun `passes if the subject is equal to the expected value`() {
      expect(1).isGreaterThanOrEqualTo(1)
    }

    @Test
    fun `fails if the subject is less than the expected value`() {
      fails {
        expect(LocalDate.of(2018, 5, 1)).isGreaterThanOrEqualTo(LocalDate.of(2018, 5, 2))
      }
    }
  }

  @Nested
  @DisplayName("isLessThanOrEqualTo assertion")
  inner class IsLessThanOrEqualTo {
    @Test
    fun `passes if the subject is less than the expected value`() {
      expect(0).isLessThanOrEqualTo(1)
    }

    @Test
    fun `passes if the subject is equal to the expected value`() {
      expect(1).isLessThanOrEqualTo(1)
    }

    @Test
    fun `fails if the subject is greater than the expected value`() {
      fails {
        expect(LocalDate.of(2018, 5, 2)).isLessThanOrEqualTo(LocalDate.of(2018, 5, 1))
      }
    }
  }
}
