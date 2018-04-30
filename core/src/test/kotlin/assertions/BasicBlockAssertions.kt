package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object BasicBlockAssertions : Spek({
  describe("isNull assertion") {
    it("passes if the target is null") {
      passes {
        val target: Any? = null
        expect(target) {
          isNull()
        }
      }
    }
    it("fails if the target is not null") {
      fails {
        val target: Any? = "covfefe"
        expect(target) {
          isNull()
        }
      }.let { e ->
        assert(e.assertionCount == 1) { "Expected 1 assertion but found ${e.assertionCount}" }
        assert(e.passCount == 0) { "Expected 0 passed assertions but found ${e.passCount}" }
        assert(e.failureCount == 1) { "Expected 1 failed assertion but found ${e.failureCount}" }
      }
    }
  }

  describe("isNotNull assertion") {
    it("fails if the target is null") {
      fails {
        val target: Any? = null
        expect(target) {
          isNotNull()
        }
      }
    }
    it("passes if the target is not null") {
      passes {
        val target: Any? = "covfefe"
        expect(target) {
          isNotNull()
        }
      }
    }
  }

  describe("isA assertion") {
    it("fails if the target is null") {
      fails {
        val target: Any? = null
        expect(target) {
          isA<String>()
        }
      }
    }
    it("fails if the target is a different type") {
      fails {
        val target: Any? = 1L
        expect(target) {
          isA<String>()
        }
      }
    }
    it("passes if the target is the same exact type") {
      passes {
        val target: Any? = "covfefe"
        expect(target) {
          isA<String>()
        }
      }
    }
    it("passes if the target is a sub-type") {
      passes {
        val target: Any? = 1L
        expect(target) {
          isA<Number>()
        }
      }
    }
  }

  describe("multiple assertions in a block") {
    fails {
      val target: Any? = "covfefe"
      expect(target) {
        isNull()
        isA<Number>()
        isA<String>().hasLength(1)
      }
    }
      .let { e ->
        assert(e.assertionCount == 4) { "Expected 4 assertions but found ${e.assertionCount}" }
        assert(e.passCount == 1) { "Expected 1 passed assertion but found ${e.passCount}" }
        assert(e.failureCount == 3) { "Expected 3 failed assertions but found ${e.failureCount}" }
      }
  }
})

internal fun passes(function: () -> Unit) {
  function.invoke()
}

internal fun fails(function: () -> Unit): AssertionFailed {
  try {
    function.invoke()
    assert(false) { "Should have failed" }
    throw IllegalStateException("Unreachable statement unless assertions are not enabled")
  } catch (e: AssertionFailed) {
    return e
  }
}