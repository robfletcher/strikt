package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object CollectionAssertions : Spek({
  describe("hasSize assertion") {
    it("fails if the target size is not the expected size") {
      fails {
        val target = setOf("catflap", "rubberplant", "marzipan")
        expect(target).hasSize(1)
      }
    }
  }

  describe("allMatch assertion") {
    it("passes if all elements conform") {
      passes {
        val target = setOf("catflap", "rubberplant", "marzipan")
        expect(target).allMatch {
          isLowerCase()
        }
      }
    }
    it("fails if any element does not conform") {
      fails {
        val target = setOf("catflap", "rubberplant", "marzipan")
        expect(target).allMatch {
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