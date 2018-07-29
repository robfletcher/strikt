package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.fails

@DisplayName("assertions on Collection")
internal class CollectionAssertions {
  @Nested
  @DisplayName("hasSize assertion")
  inner class HasSize {
    @Test
    fun `fails if the subject size is not the expected size`() {
      fails {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject).hasSize(1)
      }
    }

    @Nested
    @DisplayName("isEmpty assertion")
    inner class IsEmpty {
      @Test
      fun `passes if collection is empty`() {
        expect(emptyList<AnyAssertions>()).isEmpty()
      }

      @Test
      fun `fails if the collection is not empty`() {
        fails {
          expect(listOf("catflap", "rubberplant", "marzipan")).isEmpty()
        }
      }
    }

    @Nested
    @DisplayName("isNotEmpty assertion")
    inner class IsNotEmpty {
      @Test
      fun `fails if collection is empty`() {
        fails {
          expect(emptyList<AnyAssertions>()).isNotEmpty()
        }
      }

      @Test
      fun `passes if the collection is not empty`() {
        expect(listOf("catflap", "rubberplant", "marzipan")).isNotEmpty()
      }
    }
  }
}
