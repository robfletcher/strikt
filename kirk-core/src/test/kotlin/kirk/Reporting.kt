package kirk

import kirk.api.expect
import kirk.assertions.all
import kirk.assertions.hasSize
import kirk.assertions.isUpperCase
import kirk.assertions.startsWith
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import kotlin.test.assertEquals

object Reporting : Spek({

  describe("assertion failure messages") {
    on("evaluating a block assertion with multiple failures") {

      val e = fails {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject) {
          hasSize(0)
          all { isUpperCase() }
          all { startsWith('c') }
        }
      }

      it("reports assertion statistics") {
        assertEquals(7, e.assertionCount, "Assertions")
        assertEquals(1, e.passCount, "Passed")
        assertEquals(6, e.failureCount, "Failed")
      }

      it("formats the error message") {
        // TODO: comparing big blocks of text is shitty
        val expectedLines = listOf(
          "✘ [catflap, rubberplant, marzipan] has size 0",
          "✘ [catflap, rubberplant, marzipan] all elements match predicate",
          "  ✘ catflap is upper case",
          "  ✘ rubberplant is upper case",
          "  ✘ marzipan is upper case",
          "✘ [catflap, rubberplant, marzipan] all elements match predicate",
          "  ✔ catflap starts with 'c'",
          "  ✘ rubberplant starts with 'c'",
          "  ✘ marzipan starts with 'c'",
          ""
        )
        val actualLines = e.message.lines()
        assertEquals(
          expectedLines.size,
          actualLines.size,
          "Expected ${expectedLines.size} lines of output but found ${actualLines.size}"
        )
        actualLines.forEachIndexed { i, line ->
          assertEquals(
            expectedLines[i],
            line,
            "Assertion failure message line ${i + 1} was\n\"$line\""
          )
        }
      }
    }
  }

})