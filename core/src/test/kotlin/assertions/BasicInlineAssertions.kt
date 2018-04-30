package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object BasicInlineAssertions : Spek({
  describe("isNull assertion") {
    it("passes if the target is null") {
      passes {
        val target: Any? = null
        expect(target).isNull()
      }
    }
    it("fails if the target is not null") {
      fails {
        val target: Any? = "covfefe"
        expect(target).isNull()
      }
    }
  }

  describe("isNotNull assertion") {
    it("fails if the target is null") {
      fails {
        val target: Any? = null
        expect(target).isNotNull()
      }
    }
    it("passes if the target is not null") {
      passes {
        val target: Any? = "covfefe"
        expect(target).isNotNull()
      }
    }
  }

  describe("isA assertion") {
    it("fails if the target is null") {
      fails {
        val target: Any? = null
        expect(target).isA<String>()
      }
    }
    it("fails if the target is a different type") {
      fails {
        val target = 1L
        expect(target).isA<String>()
      }
    }
    it("passes if the target is the same exact type") {
      passes {
        val target = "covfefe"
        expect(target).isA<String>()
      }
    }
    it("passes if the target is a sub-type") {
      passes {
        val target: Any = 1L
        expect(target).isA<Number>()
      }
    }
    it("passes if the multiple isA assertions pass") {
      passes {
        val target: Any = 1L
        expect(target).isA<Number>().isA<Long>()
      }
    }
    it("fails if the chained isA does not hold") {
      fails {
        val target: Any = 1L
        expect(target).isA<Number>().isA<Int>()
      }
    }
  }
})