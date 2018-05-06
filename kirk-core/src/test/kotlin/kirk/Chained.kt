package kirk

import kirk.api.expect
import kirk.assertions.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object Chained : Spek({
  describe("chained assertions") {
    it("stops on the first failed assertion in the chain") {
      fails {
        val subject: Any? = null
        expect(subject).isNotNull().isA<String>()
      }.let { e ->
        assert(e.assertionCount == 1) { "Expected 1 assertion but found ${e.assertionCount}" }
        assert(e.passCount == 0) { "Expected 0 passed assertions but found ${e.passCount}" }
        assert(e.failureCount == 1) { "Expected 1 failed assertion but found ${e.failureCount}" }
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