package assertions

import assertions.api.expect
import assertions.assertions.isA
import assertions.assertions.isNotNull
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object Chained : Spek({
  describe("isNotNull assertion") {
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
  }
})