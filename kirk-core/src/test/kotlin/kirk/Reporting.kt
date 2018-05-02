package kirk

import kirk.api.expect
import kirk.assertions.all
import kirk.assertions.hasSize
import kirk.assertions.isUpperCase
import kirk.assertions.startsWith
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

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
        assert(e.assertionCount == 7) { "Expected 4 assertions but found ${e.assertionCount}" }
        assert(e.passCount == 1) { "Expected 1 passed assertion but found ${e.passCount}" }
        assert(e.failureCount == 6) { "Expected 4 failed assertions but found ${e.failureCount}" }
      }

      it("formats the error message") {
        // TODO: comparing big blocks of text is shitty
        val expectedMessage = """✘ [catflap, rubberplant, marzipan] has size 0
✘ [catflap, rubberplant, marzipan] all elements match predicate:
  ✘ catflap is upper case
  ✘ rubberplant is upper case
  ✘ marzipan is upper case
✘ [catflap, rubberplant, marzipan] all elements match predicate:
  ✔ catflap starts with 'c'
  ✘ rubberplant starts with 'c'
  ✘ marzipan starts with 'c'"""
        assert(e.message == expectedMessage) {
          "Assertion failure message was\n${e.message}"
        }
      }
    }
  }

})