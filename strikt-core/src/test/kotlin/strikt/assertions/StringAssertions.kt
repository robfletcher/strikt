package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.internal.opentest4j.CompoundAssertionFailure

@DisplayName("assertions on String")
internal class StringAssertions {
  @Nested
  @DisplayName("isEqualToIgnoringCase assertion")
  inner class IsEqualToIgnoringCase {
    @Test
    fun `passes if the subject is identical to the expected value`() {
      expectThat("fnord").isEqualToIgnoringCase("fnord")
    }

    @Test
    fun `fails if the subject is different`() {
      assertThrows<AssertionError> {
        expectThat("despite the negative press fnord")
          .isEqualToIgnoringCase("fnord")
      }
    }

    @Test
    fun `passes if the subject is the same as the expected value apart from case`() {
      expectThat("fnord").isEqualToIgnoringCase("fnord")
    }

    @Test
    fun `can expect string start`() {
      expectThat("fnord").startsWith("fno")
    }

    @Test
    fun `can expect string end`() {
      expectThat("fnord").endsWith("nord")
    }

    @Test
    fun `outputs real end when endsWith fails`() {
      expectThat(assertThrows<AssertionError> {
        expectThat("fnord").endsWith("nor")
      }).message.contains("""ends with "nor" : found "ord"""")
    }

    @Test
    fun `outputs real start when startsWith fails`() {
      expectThat(assertThrows<AssertionError> {
        expectThat("fnord").startsWith("fnrd")
      }).message.contains("""starts with "fnrd" : found "fnor"""")
    }
  }

  @Nested
  @DisplayName("block assertions on string subjects")
  inner class Blocks {
    @Test
    fun compiles() {
      val error = assertThrows<CompoundAssertionFailure> {
        val subject = "The Enlightened take things Lightly"
        expectThat(subject = subject) {
          hasLength(5)
          matches(Regex("\\d+"))
          startsWith("T")
        }
      }
      assertEquals(2, error.failures.size)
    }
  }

  @Test
  fun `can trim string`() {
    expectThat(" fnord ").trim().isEqualToIgnoringCase("fnord")
  }
}
