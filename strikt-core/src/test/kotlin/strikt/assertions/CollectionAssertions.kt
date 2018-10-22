package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

@DisplayName("assertions on Collection")
internal class CollectionAssertions {
  @Nested
  @DisplayName("hasSize assertion")
  inner class HasSize {
    @Test
    fun `fails if the subject size is not the expected size`() {
      assertThrows<AssertionError>{
    val subject = setOf("catflap", "rubberplant", "marzipan")
    expectThat(subject).hasSize(1)
  }
    }

    @Nested
    @DisplayName("isNullOrEmpty assertion")
    inner class IsEmpty {
      @Test
      fun `passes if collection is empty`() {
        expectThat(emptyList<AnyAssertions>()).isEmpty()
      }

      @Test
      fun `fails if the collection is not empty`() {
        assertThrows<AssertionError>{
    expectThat(listOf("catflap", "rubberplant", "marzipan")).isEmpty()
  }
      }
    }

    @Nested
    @DisplayName("isNotEmpty assertion")
    inner class IsNotEmpty {
      @Test
      fun `fails if collection is empty`() {
        assertThrows<AssertionError>{
    expectThat(emptyList<AnyAssertions>()).isNotEmpty()
  }
      }

      @Test
      fun `passes if the collection is not empty`() {
        expectThat(listOf("catflap", "rubberplant", "marzipan")).isNotEmpty()
      }
    }
  }
}
