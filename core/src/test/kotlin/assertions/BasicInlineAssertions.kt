package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object BasicInlineAssertions : Spek({
  describe("isNull assertion") {
    it("passes if the target is null") {
      shouldPass {
        val target: String? = null
        expect(target).isNull()
      }
    }
    it("fails if the target is not null") {
      shouldFail {
        val target = "covfefe"
        expect(target).isNull()
      }
    }
  }

  describe("isNotNull assertion") {
    it("fails if the target is null") {
      shouldFail {
        val target: String? = null
        expect(target).isNotNull()
      }
    }
    it("passes if the target is not null") {
      shouldPass {
        val target = "covfefe"
        expect(target).isNotNull()
      }
    }
  }
})