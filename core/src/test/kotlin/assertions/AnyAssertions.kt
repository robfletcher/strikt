package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object AnyAssertions : Spek({

  describe("isNull assertion") {
    it("passes if the subject is null") {
      passes {
        val subject: Any? = null
        expect(subject).isNull()
      }
    }
    it("fails if the subject is not null") {
      fails {
        val subject: Any? = "covfefe"
        expect(subject).isNull()
      }
    }
    it("down-casts the result") {
      val subject: Any? = null
      expect(subject)
        .also { assert(it is Assertion<Any?>) }
        .isNull()
        .also { assert(it is Assertion<Nothing>) }
    }
  }

  describe("isNotNull assertion") {
    it("fails if the subject is null") {
      fails {
        val subject: Any? = null
        expect(subject).isNotNull()
      }
    }
    it("passes if the subject is not null") {
      passes {
        val subject: Any? = "covfefe"
        expect(subject).isNotNull()
      }
    }
    it("down-casts the result") {
      val subject: Any? = "covfefe"
      expect(subject)
        .also { assert(it is Assertion<Any?>) }
        .isNotNull()
        .also { assert(it is Assertion<Any>) }
    }
  }

  describe("isA assertion") {
    it("fails if the subject is null") {
      fails {
        val subject: Any? = null
        expect(subject).isA<String>()
      }
    }
    it("fails if the subject is a different type") {
      fails {
        val subject = 1L
        expect(subject).isA<String>()
      }
    }
    it("passes if the subject is the same exact type") {
      passes {
        val subject = "covfefe"
        expect(subject).isA<String>()
      }
    }
    it("passes if the subject is a sub-type") {
      passes {
        val subject: Any = 1L
        expect(subject).isA<Number>()
      }
    }
    it("down-casts the result") {
      val subject: Any = 1L
      expect(subject)
        .also { assert(it is Assertion<Any>) }
        .isA<Number>()
        .also { assert(it is Assertion<Number>) }
        .isA<Long>()
        .also { assert(it is Assertion<Long>) }
    }
    it("allows specialized assertions after establishing type") {
      passes {
        val subject: Any = "covfefe"
        expect(subject)
          .also { assert(it is Assertion<Any>) }
          .isA<String>()
          .also { assert(it is Assertion<String>) }
          .hasLength(7) // only available on Assertion<CharSequence>
      }
    }
  }

  describe("isEqualTo assertion") {
    it("passes if the subject matches the expectation") {
      passes {
        expect("covfefe").isEqualTo("covfefe")
      }
    }
    it("fails if the subject does not match the expectation") {
      fails {
        expect("covfefe").isEqualTo("COVFEFE")
      }
    }
    it("fails if the subject is a different type to the expectation") {
      fails {
        expect(1).isEqualTo(1L)
      }
    }
    it("can be used on a null subject") {
      fails {
        expect(null).isEqualTo("covfefe")
      }
    }
    it("can be used with a null expected value") {
      fails {
        expect("covfefe").isEqualTo(null)
      }
    }
  }
})
