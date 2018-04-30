package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object AnyAssertions : Spek({

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
    it("down-casts the result") {
      val target: Any? = null
      expect(target)
        .also { assert(it is Assertion<Any?>) }
        .isNull()
        .also { assert(it is Assertion<Nothing>) }
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
    it("down-casts the result") {
      val target: Any? = "covfefe"
      expect(target)
        .also { assert(it is Assertion<Any?>) }
        .isNotNull()
        .also { assert(it is Assertion<Any>) }
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
    it("down-casts the result") {
      val target: Any = 1L
      expect(target)
        .also { assert(it is Assertion<Any>) }
        .isA<Number>()
        .also { assert(it is Assertion<Number>) }
        .isA<Long>()
        .also { assert(it is Assertion<Long>) }
    }
    it("allows specialized assertions after establishing type") {
      passes {
        val target: Any = "covfefe"
        expect(target)
          .also { assert(it is Assertion<Any>) }
          .isA<String>()
          .also { assert(it is Assertion<String>) }
          .hasLength(7) // only available on Assertion<CharSequence>
      }
    }
  }
})
