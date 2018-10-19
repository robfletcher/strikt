package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.fails

@DisplayName("Assertions on Map")
internal class MapAssertions {
  @Nested
  @DisplayName("isNullOrEmpty assertion")
  inner class IsEmpty {
    @Test
    fun `passes if the subject is empty`() {
      val subject = emptyMap<Any, Any>()
      expectThat(subject).isEmpty()
    }

    @Test
    fun `fails if the subject is not empty`() {
      fails {
        val subject = mapOf("Eris" to "Strife and confusion")
        expectThat(subject).isEmpty()
      }
    }
  }

  @Nested
  @DisplayName("isNotEmpty assertion")
  inner class IsNotEmpty {
    @Test
    fun `fails if the subject is empty`() {
      fails {
        val subject = emptyMap<Any, Any>()
        expectThat(subject).isNotEmpty()
      }
    }

    @Test
    fun `passes if the subject is not empty`() {
      val subject = mapOf("Eris" to "Strife and confusion")
      expectThat(subject).isNotEmpty()
    }
  }

  @Nested
  @DisplayName("containsKey assertion")
  inner class ContainsKey {
    @Test
    fun `passes if the subject has a matching key`() {
      val subject = mapOf("foo" to "bar")
      expectThat(subject).containsKey("foo")
    }

    @Test
    fun `fails if the subject does not have a matching key`() {
      val error = fails {
        val subject = emptyMap<Any, Any>()
        expectThat(subject).containsKey("foo")
      }
      assertEquals(
        "▼ Expect that {}:\n" +
          "  ✗ has an entry with the key \"foo\"",
        error.message
      )
    }
  }

  @Nested
  @DisplayName("containsKeys assertion")
  inner class ContainsKeys {
    @Test
    fun `passes if the subject has all the specified keys`() {
      val subject =
        mapOf("foo" to "bar", "baz" to "fnord", "qux" to "fnord")
      expectThat(subject).containsKeys("foo", "baz")
    }

    @Test
    fun `fails if the subject does not have a matching key`() {
      val error = fails {
        val subject =
          mapOf("foo" to "bar", "baz" to "fnord", "qux" to "fnord")
        expectThat(subject).containsKeys("foo", "bar", "fnord")
      }
      assertEquals(
        "▼ Expect that {\"foo\"=\"bar\", \"baz\"=\"fnord\", \"qux\"=\"fnord\"}:\n" +
          "  ✗ has entries with the keys [\"foo\", \"bar\", \"fnord\"]\n" +
          "    ✓ has an entry with the key \"foo\"\n" +
          "    ✗ has an entry with the key \"bar\"\n" +
          "    ✗ has an entry with the key \"fnord\"",
        error.message
      )
    }
  }

  @Nested
  @DisplayName("hasEntry assertion")
  inner class HasEntry {
    @Test
    fun `passes if the subject has a matching key value pair`() {
      val subject = mapOf("foo" to "bar")
      expectThat(subject).hasEntry("foo", "bar")
    }

    @Test
    fun `fails if the subject does not have a matching key`() {
      val error = fails {
        val subject = emptyMap<Any, Any>()
        expectThat(subject).hasEntry("foo", "bar")
      }
      assertEquals(
        "▼ Expect that {}:\n" +
          "  ✗ has an entry with the key \"foo\"",
        error.message
      )
    }

    @Test
    fun `fails if the subject has a different value for the key`() {
      val error = fails {
        val subject = mapOf("foo" to "bar")
        expectThat(subject).hasEntry("foo", "baz")
      }
      assertEquals(
        "▼ Expect that {\"foo\"=\"bar\"}:\n" +
          "  ✓ has an entry with the key \"foo\"\n" +
          "  ▼ entry [\"foo\"]:\n" +
          "    ✗ is equal to \"baz\" : found \"bar\"",
        error.message
      )
    }
  }
}
