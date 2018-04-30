package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object CollectionAssertions : Spek({
  describe("hasSize assertion") {
    it("fails if the subject size is not the expected size") {
      fails {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject).hasSize(1)
      }
    }
  }

  describe("allMatch assertion") {
    it("passes if all elements conform") {
      passes {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject).allMatch {
          isLowerCase()
        }
      }
    }
    it("fails if any element does not conform") {
      fails {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject).allMatch {
          isLowerCase()
          startsWith('c')
        }
      }
        .let { failure ->
          assert(failure.assertionCount == 6) { "Expected 6 assertions but found ${failure.assertionCount}" }
          assert(failure.passCount == 4) { "Expected 4 passed assertions but found ${failure.passCount}" }
          assert(failure.failureCount == 2) { "Expected 2 failed assertions but found ${failure.failureCount}" }
        }
    }
  }
})