package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object CollectionInlineAssertions : Spek({
  describe("hasSize assertion") {
    it("fails if the target size is not the expected size") {
      shouldFail {
        val target = setOf("catflap", "rubberplant", "marzipan")
        expect(target).hasSize(1)
      }
    }
  }

  describe("allMatch assertion") {
    it("passes if all elements conform") {
      shouldPass {
        val target = setOf("catflap", "rubberplant", "marzipan")
        expect(target).allMatch {
          isLowerCase()
        }
      }
    }
    it("fails if any element does not conform") {
      shouldFail {
        val target = setOf("catflap", "rubberplant", "marzipan")
        expect(target).allMatch {
          startsWith('c')
        }
      }
    }
  }
})