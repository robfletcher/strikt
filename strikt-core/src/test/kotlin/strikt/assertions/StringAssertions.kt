package strikt.assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import strikt.api.expect
import strikt.fails

internal object StringAssertions : Spek({
  describe("assertions on ${String::class.simpleName}") {
    describe("isEqualToIgnoringCase assertion") {
      it("passes if the subject is identical to the expected value") {
        expect("covfefe").isEqualToIgnoringCase("covfefe")
      }
      it("fails if the subject is different") {
        fails {
          expect("despite the negative press covfefe").isEqualToIgnoringCase("covfefe")
        }
      }
      it("passes if the subject is the same as the expected value apart from case") {
        expect("covfefe").isEqualToIgnoringCase("COVFEFE")
      }
    }
  }
})