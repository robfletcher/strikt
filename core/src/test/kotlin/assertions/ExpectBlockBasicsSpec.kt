package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object ExpectBlockBasicsSpec : Spek({

  describe("isNull assertion") {
    it("passes if the target is null") {
      shouldPass {
        val target: String? = null
        expect(target) {
          isNull()
        }
      }
    }
    it("fails if the target is not null") {
      shouldFail {
        val target = "covfefe"
        expect(target) {
          isNull()
        }
      }
    }
  }

  describe("isNotNull assertion") {
    it("fails if the target is null") {
      shouldFail {
        val target: String? = null
        expect(target) {
          isNotNull()
        }
      }
    }
    it("passes if the target is not null") {
      shouldPass {
        val target = "covfefe"
        expect(target) {
          isNotNull()
        }
      }
    }
  }
})

internal fun shouldPass(function: (() -> Unit)) {
  function.invoke()
}

internal fun shouldFail(function: (() -> Unit)) {
  try {
    function.invoke()
    throw AssertionError("Should have failed")
  } catch (e: AssertionError) {
    // expected
  }
}