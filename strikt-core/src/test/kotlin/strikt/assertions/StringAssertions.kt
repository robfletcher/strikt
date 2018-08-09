package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expect
import strikt.fails
import strikt.internal.opentest4j.CompoundAssertionFailure

@DisplayName("assertions on String")
internal class StringAssertions {
  @Nested
  @DisplayName("isEqualToIgnoringCase assertion")
  inner class IsEqualToIgnoringCase {
    @Test
    fun `passes if the subject is identical to the expected value`() {
      expect("fnord").isEqualToIgnoringCase("fnord")
    }

    @Test
    fun `fails if the subject is different`() {
      fails {
        expect("despite the negative press fnord").isEqualToIgnoringCase("fnord")
      }
    }

    @Test
    fun `passes if the subject is the same as the expected value apart from case`() {
      expect("fnord").isEqualToIgnoringCase("fnord")
    }
  }

  @Nested
  @DisplayName("block assertions on string subjects")
  inner class Blocks {
    @Test
    fun `compiles`() {
      assertThrows<CompoundAssertionFailure> {
        val subject = "The Enlightened take things Lightly"
        expect(subject = subject) {
          hasLength(5)
          matches(Regex("\\d+"))
          startsWith("T")
        }
      }.let { e ->
        assertEquals(2, e.failures.size)
      }
    }
  }
}
