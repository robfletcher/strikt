package strikt.assertions

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
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
      it("fails when the subject is null") {
        fails {
          expect(null).isTrue()
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
      it("fails when the subject is null") {
        fails {
          expect(null).isFalse()
        }
      }
    }
  }
})
