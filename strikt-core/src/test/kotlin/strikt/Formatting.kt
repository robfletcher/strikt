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
        "  ✗ all elements match:\n" +
        "    ▼ Expect that \"catflap\":\n" +
        "      ✗ is upper case\n" +
        "    ▼ Expect that \"rubberplant\":\n" +
        "      ✗ is upper case\n" +
        "    ▼ Expect that \"marzipan\":\n" +
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
        "    ▼ Expect that \"catflap\":\n" +
        "      ✗ is upper case\n" +
        "    ▼ Expect that \"rubberplant\":\n" +
        "      ✗ is upper case\n" +
        "      ✗ starts with 'c'\n" +
        "    ▼ Expect that \"marzipan\":\n" +
        "      ✗ is upper case\n" +
        "      ✗ starts with 'c'"
    assertEquals(expected, e.message)
  }

  @Test
  fun `passing assertions are excluded by default`() {
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
        "  ✗ all elements match:\n" +
        "    ▼ Expect that \"rubberplant\":\n" +
        "      ✗ starts with 'c'\n" +
        "    ▼ Expect that \"marzipan\":\n" +
        "      ✗ starts with 'c'"
    assertEquals(expected, e.message)
  }

  @Test
  fun `passing assertions are included in verbose mode`() {
    val e = try {
      System.setProperty("strikt.verbose", "true")

      fails {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject) {
          hasSize(3)
          all {
            startsWith('c')
          }
        }
      }
    } finally {
      System.clearProperty("strikt.verbose")
    }

    val expected =
      "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
        "  ✓ has size 3\n" +
        "  ✗ all elements match:\n" +
        "    ▼ Expect that \"catflap\":\n" +
        "      ✓ starts with 'c'\n" +
        "    ▼ Expect that \"rubberplant\":\n" +
        "      ✗ starts with 'c'\n" +
        "    ▼ Expect that \"marzipan\":\n" +
        "      ✗ starts with 'c'"

    assertEquals(expected, e.message)
  }
}
