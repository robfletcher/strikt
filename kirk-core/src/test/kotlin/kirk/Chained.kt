package kirk

import kirk.api.expect
import kirk.assertions.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import kotlin.test.assertEquals

internal object Chained : Spek({
  describe("chained assertions") {
    it("stops on the first failed assertion in the chain") {
      fails {
        val subject: Any? = null
        expect(subject).isNotNull().isA<String>()
      }.let { e ->
        assertEquals(1, e.assertionCount, "Assertions")
        assertEquals(0, e.passCount, "Passed")
        assertEquals(1, e.failureCount, "Failed")
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