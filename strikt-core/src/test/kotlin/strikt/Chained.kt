package strikt

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import strikt.api.expect
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isUpperCase

internal object Chained : Spek({
  describe("chained assertions") {
    it("stops on the first failed assertion in the chain") {
      fails {
        val subject: Any? = null
        expect(subject).isNotNull().isA<String>()
      }
    }

    describe("'not()' function") {
      it("can be negated") {
        fails {
          val subject: Any? = null
          expect(subject).not().isNull()
        }
      }

      it("affects the entire chain") {
        val subject = "covfefe"
        expect(subject).not().isUpperCase().isA<Int>().isEqualTo(1L)
      }
    }
  }
})
