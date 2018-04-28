package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object ExpectBlockBasicsSpec : Spek({

  describe("assertions made on a target in a block") {
    on("asserting null is null") {
      it("passes") {
        shouldPass {
          val target: String? = null
          expect(target) {
            isNull()
          }
        }
      }
    }

    on("asserting null is not null") {
      it("fails") {
        shouldFail {
          val target: String? = null
          expect(target) {
            isNotNull()
          }
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