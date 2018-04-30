package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object BlockAssertions : Spek({
  describe("assertions in blocks") {
    it("evaluates all assertions in the block even if some fail") {
      fails {
        val target: Any? = "covfefe"
        expect(target) {
          isNull()
          isNotNull()
          isA<String>()
          isA<Number>()
        }
      }.let { e ->
        assert(e.assertionCount == 4) { "Expected 4 assertions but found ${e.assertionCount}" }
        assert(e.passCount == 2) { "Expected 2 passed assertions but found ${e.passCount}" }
        assert(e.failureCount == 2) { "Expected 2 failed assertions but found ${e.failureCount}" }
      }
    }
  }
})
