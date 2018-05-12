package kirk

import kirk.api.expect
import kirk.assertions.isA
import kirk.assertions.isNotNull
import kirk.assertions.isNull
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object Block : Spek({
  describe("assertions in blocks") {
    it("evaluates all assertions in the block even if some fail") {
      fails {
        val subject: Any? = "covfefe"
        expect(subject) {
          isNull()
          isNotNull()
          isA<String>()
          isA<Number>()
        }
      }
    }
  }
})
