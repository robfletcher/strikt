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
      "Expect that: a couple of words (1 failure)\n" +
        "\tall elements match: (3 failures)\n" +
        "\tExpect that: \"catflap\" (1 failure)\n" +
        "\tis upper case\n" +
        "\tExpect that: \"rubberplant\" (1 failure)\n" +
        "\tis upper case\n" +
        "\tExpect that: \"marzipan\" (1 failure)\n" +
        "\tis upper case"
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

    val expected = "Expect that: [\"catflap\", \"rubberplant\", \"marzipan\"] (2 failures)\n" +
      "\thas size 0 : found 3\n" +
      "\tall elements match: (3 failures)\n" +
      "\tExpect that: \"catflap\" (1 failure)\n" +
      "\tis upper case\n" +
      "\tExpect that: \"rubberplant\" (2 failures)\n" +
      "\tis upper case\n" +
      "\tstarts with 'c'\n" +
      "\tExpect that: \"marzipan\" (2 failures)\n" +
      "\tis upper case\n" +
      "\tstarts with 'c'"
    assertEquals(expected, e.message)
  }
}
