package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.all
import strikt.assertions.hasSize
import strikt.assertions.isUpperCase
import strikt.assertions.startsWith

@DisplayName("error message formatting")
internal class Formatting {

  @Test
  fun `a failing chained assertion formats the message correctly`() {
    val e = fails {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expect(subject)
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
      expect(subject) {
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
        "      ✗ starts with 'c'\n" +
        "    ▼ \"marzipan\":\n" +
        "      ✗ is upper case\n" +
        "      ✗ starts with 'c'"
    assertEquals(expected, e.message)
  }

  @Test
  fun `passing assertions are included in the error message`() {
    val e = fails {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expect(subject) {
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
        "      ✗ starts with 'c'\n" +
        "    ▼ \"marzipan\":\n" +
        "      ✗ starts with 'c'"

    assertEquals(expected, e.message)
  }
}
