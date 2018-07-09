package strikt

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import strikt.api.expect
import strikt.assertions.isA
import strikt.assertions.isNotNull
import strikt.assertions.isNull

internal object Block : Spek({
  describe("assertions in blocks") {
    it("evaluates all assertions in the block even if some fail") {
      fails {
        val subject: Any? = "fnord"
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
