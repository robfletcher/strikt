package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.fails

@DisplayName("Assertions on Map")
internal class MapAssertions {
  @Nested
  @DisplayName("isEmpty assertion")
  inner class IsEmpty {
    @Test
    fun `passes if the subject is empty`() {
      val subject = emptyMap<Any, Any>()
      expect(subject).isEmpty()
    }

    @Test
    fun `fails if the subject is not empty`() {
      fails {
        val subject = mapOf("Eris" to "Strife and confusion")
        expect(subject).isEmpty()
      }
    }
  }

  @Nested
  @DisplayName("containsKey assertion")
  inner class ContainsKey {
    @Test
    fun `passes if the subject has a matching key`() {
      val subject = mapOf("foo" to "bar")
      expect(subject).containsKey("foo")
    }

    @Test
    fun `fails if the subject does not have a matching key`() {
      fails {
        val subject = emptyMap<Any, Any>()
        expect(subject).containsKey("foo")
      }.let { e ->
        assertEquals(
          "▼ Expect that {}:\n" +
            "  ✗ has an entry with the key \"foo\"",
          e.message
        )
      }
    }
  }

  @Nested
  @DisplayName("containsKeys assertion")
  inner class ContainsKeys {
    @Test
    fun `passes if the subject has all the specified keys`() {
      val subject =
        mapOf("foo" to "bar", "baz" to "fnord", "qux" to "fnord")
      expect(subject).containsKeys("foo", "baz")
    }

    @Test
    fun `fails if the subject does not have a matching key`() {
      fails {
        val subject =
          mapOf("foo" to "bar", "baz" to "fnord", "qux" to "fnord")
        expect(subject).containsKeys("foo", "bar", "fnord")
      }.let { e ->
        assertEquals(
          "▼ Expect that {foo=bar, baz=fnord, qux=fnord}:\n" +
            "  ✗ has entries with the keys [\"foo\", \"bar\", \"fnord\"]\n" +
            "    ✓ has an entry with the key \"foo\"\n" +
            "    ✗ has an entry with the key \"bar\"\n" +
            "    ✗ has an entry with the key \"fnord\"",
          e.message
        )
      }
    }
  }

  @Nested
  @DisplayName("hasEntry assertion")
  inner class HasEntry {
    @Test
    fun `passes if the subject has a matching key value pair`() {
      val subject = mapOf("foo" to "bar")
      expect(subject).hasEntry("foo", "bar")
    }

    @Test
    fun `fails if the subject does not have a matching key`() {
      fails {
        val subject = emptyMap<Any, Any>()
        expect(subject).hasEntry("foo", "bar")
      }.let { e ->
        assertEquals(
          "▼ Expect that {}:\n" +
            "  ✗ has an entry with the key \"foo\"",
          e.message
        )
      }
    }

    @Test
    fun `fails if the subject has a different value for the key`() {
      fails {
        val subject = mapOf("foo" to "bar")
        expect(subject).hasEntry("foo", "baz")
      }.let { e ->
        assertEquals(
          "▼ Expect that {foo=bar}:\n" +
            "  ✓ has an entry with the key \"foo\"\n" +
            "  ▼ entry [\"foo\"]:\n" +
            "    ✗ is equal to \"baz\" : found \"bar\"",
          e.message
        )
      }
    }
  }
}
