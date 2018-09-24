package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.all
import strikt.assertions.contains
import strikt.assertions.hasLength
import strikt.assertions.hasSize
import strikt.assertions.isNotEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isUpperCase
import strikt.assertions.startsWith

@DisplayName("error message formatting")
internal class Formatting {

  @Test
  fun `a failing chained assertion formats the message correctly`() {
    val e = fails {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expectThat(subject)
        .describedAs("a couple of words")
        .hasSize(3)
        .all { isUpperCase() }
        .all { startsWith('c') }
    }

    val expected =
      "▼ Expect that a couple of words:\n" +
        "  ✓ has size 3\n" +
        "  ✗ all elements match:\n" +
        "    ▼ \"catflap\":\n" +
        "      ✗ is upper case\n" +
        "    ▼ \"rubberplant\":\n" +
        "      ✗ is upper case\n" +
        "    ▼ \"marzipan\":\n" +
        "      ✗ is upper case"
    assertEquals(expected, e.message)
  }

  @Test
  fun `a failing block assertion formats the message correctly`() {
    val e = fails {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expectThat(subject) {
        hasSize(0)
        all {
          isUpperCase()
          startsWith('c')
        }
      }
    }

    val expected =
      "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
        "  ✗ has size 0 : found 3\n" +
        "  ✗ all elements match:\n" +
        "    ▼ \"catflap\":\n" +
        "      ✗ is upper case\n" +
        "      ✓ starts with 'c'\n" +
        "    ▼ \"rubberplant\":\n" +
        "      ✗ is upper case\n" +
        "      ✗ starts with 'c' : found 'r'\n" +
        "    ▼ \"marzipan\":\n" +
        "      ✗ is upper case\n" +
        "      ✗ starts with 'c' : found 'm'"
    assertEquals(expected, e.message)
  }

  @Test
  fun `passing assertions are included in the error message`() {
    val e = fails {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expectThat(subject) {
        hasSize(3)
        all {
          startsWith('c')
        }
      }
    }

    val expected =
      "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
        "  ✓ has size 3\n" +
        "  ✗ all elements match:\n" +
        "    ▼ \"catflap\":\n" +
        "      ✓ starts with 'c'\n" +
        "    ▼ \"rubberplant\":\n" +
        "      ✗ starts with 'c' : found 'r'\n" +
        "    ▼ \"marzipan\":\n" +
        "      ✗ starts with 'c' : found 'm'"

    assertEquals(expected, e.message)
  }

  @Test
  fun `an own toString is preferred to mapping over iterable`() {
    val toStringOutput = "useful toString info"
    val iteratorOutput = "less useful iterator output"

    class IterableWithToString : Iterable<String> {
      override fun iterator(): Iterator<String> = listOf(iteratorOutput).iterator()
      override fun toString(): String = toStringOutput
    }

    val e = fails {
      val subject = IterableWithToString()
      expectThat(subject) { isNotEqualTo(subject) }
    }

    expectThat(e.message).isNotNull().and {
      contains(toStringOutput)
      not().contains(iteratorOutput)
    }
  }

  @Test
  fun `iterable is used when there is no own toString method`() {
    val iteratorOutput = "useful iterable info"

    class IterableWithToString : Iterable<String> {
      override fun iterator(): Iterator<String> = listOf(iteratorOutput).iterator()
    }

    val e = fails {
      val subject = IterableWithToString()
      expectThat(subject) { isNotEqualTo(subject) }
    }

    expectThat(e.message).isNotNull().contains(iteratorOutput)
  }

  @Test
  fun `toString method prints value when it has at most 20 characters`() {
    val toStringValue = "twenty chars string!"
    val expectedMessage = "▼ Expect that \"twenty chars string!\":\n"

    val error = fails {
      class Text { override fun toString() = toStringValue }
      expectThat(Text().toString()).hasLength(10)
    }

    assertTrue(error.message.orEmpty().contains(expectedMessage))
  }

  @Test
  fun `toString method trims value when it has more than 20 characters`() {
    val actualToString = "0".repeat(141)
    val expectedToString = "0".repeat(20)
    val expectedMessage =
      "▼ Expect that \"$expectedToString...\":\n" +
        "  ✗ has length 140 : found 141"

    val error = fails {
      class Text { override fun toString() = actualToString }
      expectThat(Text().toString()).hasLength(140)
    }

    assertEquals(expectedMessage, error.message)
  }
}
