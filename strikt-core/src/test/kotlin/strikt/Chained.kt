package strikt

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import strikt.api.expect
import strikt.assertions.*

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