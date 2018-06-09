package strikt

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Assertions.assertEquals
import strikt.api.expect
import strikt.assertions.all
import strikt.assertions.hasSize
import strikt.assertions.isUpperCase
import strikt.assertions.startsWith

object Reporting : Spek({

  describe("assertion failure messages") {
    on("evaluating a chained assertion that fails") {
      val e = fails {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject)
          .describedAs("a couple of words")
          .hasSize(3)
          .all { isUpperCase() }
          .all { startsWith('c') }
      }

      it("formats the error message") {
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
    }
  }

  on("evaluating a block assertion with multiple failures") {
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

    it("formats the error message") {
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
})
