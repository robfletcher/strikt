package strikt.assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import strikt.api.expect
import strikt.fails

@Suppress("SimplifyBooleanWithConstants")
internal object BooleanAssertions : Spek({
  describe("assertions on ${Boolean::class.simpleName}") {
    describe("isTrue assertion") {
      it("passes when the subject is true") {
        expect("a" == "a").isTrue()
      }
      it("fails when the subject is false") {
        fails {
          expect("a" == "A").isTrue()
        }
      }
    }
    describe("isFalse assertion") {
      it("passes when the subject is false") {
        expect("a" == "A").isFalse()
      }
      it("fails when the subject is false") {
        fails {
          expect("a" == "a").isFalse()
        }
      }
    }
  }
})