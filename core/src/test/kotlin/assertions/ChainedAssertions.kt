package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object ChainedAssertions : Spek({
  describe("isNotNull assertion") {
    it("stops on the first failed assertion in the chain") {
      fails {
        val target: Any? = null
        expect(target).isNotNull().isA<String>()
      }.let { e ->
        assert(e.assertionCount == 1) { "Expected 1 assertion but found ${e.assertionCount}" }
        assert(e.passCount == 0) { "Expected 0 passed assertions but found ${e.passCount}" }
        assert(e.failureCount == 1) { "Expected 1 failed assertion but found ${e.failureCount}" }
      }
    }
  }
})