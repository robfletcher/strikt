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

    @Test
    fun `can expect string start`() {
      expect("fnord").startsWith("fno")
    }

    @Test
    fun `can expect string end`() {
      expect("fnord").endsWith("nord")
    }

    @Test
    fun `outputs real end when endsWith fails`() {
      expect(fails {
        expect("fnord").endsWith("nor")
      }).message.contains("""ends with "nor" : found "ord"""")
    }

    @Test
    fun `outputs real start when startsWith fails`() {
      expect(fails {
        expect("fnord").startsWith("fnrd")
      }).message.contains("""starts with "fnrd" : found "fnor"""")
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
