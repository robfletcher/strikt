package strikt.assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals
import strikt.api.Assertion
import strikt.api.expect
import strikt.fails

internal object AnyAssertions : Spek({
  describe("assertions on ${Any::class.simpleName}") {
    describe("isNull assertion") {
      it("passes if the subject is null") {
        val subject: Any? = null
        expect(subject).isNull()
      }
      it("fails if the subject is not null") {
        fails {
          val subject: Any? = "covfefe"
          expect(subject).isNull()
        }
      }
      @Suppress("USELESS_IS_CHECK")
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
        val subject: Any? = "covfefe"
        expect(subject).isNotNull()
      }
      @Suppress("USELESS_IS_CHECK")
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
        val subject = "covfefe"
        expect(subject).isA<String>()
      }
      it("passes if the subject is a sub-type") {
        val subject: Any = 1L
        expect(subject).isA<Number>()
      }
      @Suppress("USELESS_IS_CHECK")
      it("down-casts the result") {
        val subject: Any = 1L
        expect(subject)
          .also { assert(it is Assertion<Any>) }
          .isA<Number>()
          .also { assert(it is Assertion<Number>) }
          .isA<Long>()
          .also { assert(it is Assertion<Long>) }
      }
      @Suppress("USELESS_IS_CHECK")
      it("allows specialized assertions after establishing type") {
        val subject: Any = "covfefe"
        expect(subject)
          .also { assert(it is Assertion<Any>) }
          .isA<String>()
          .also { assert(it is Assertion<String>) }
          .hasLength(7) // only available on Assertion<CharSequence>
      }
    }

    describe("isEqualTo assertion") {
      it("passes if the subject matches the expectation") {
        expect("covfefe").isEqualTo("covfefe")
      }

      sequenceOf(
        Pair("covfefe", "COVFEFE"),
        Pair(1, 1L),
        Pair(null, "covfefe"),
        Pair("covfefe", null)
      ).forEach { (subject, expected) ->
        it("fails $subject is equal to $expected") {
          fails {
            expect(subject).isEqualTo(expected)
          }
        }
      }

      it("specifies type information if the values look the same") {
        fails {
          expect(5L).isEqualTo(5)
        }.let { e ->
          assertEquals("""Expect that 5 (1 failure)
	is equal to 5 (Int) : found 5 (Long)""", e.message)
        }
      }
    }

    describe("isNotEqualTo assertion") {
      it("fails if the subject matches the expectation") {
        fails {
          expect("covfefe").isNotEqualTo("covfefe")
        }
      }

      sequenceOf(
        Pair("covfefe", "COVFEFE"),
        Pair(1, 1L),
        Pair(null, "covfefe"),
        Pair("covfefe", null)
      ).forEach { (subject, expected) ->
        it("passes $subject is not equal to $expected") {
          expect(subject).isNotEqualTo(expected)
        }
      }
    }

    describe("isSameInstanceAs assertion") {

      sequenceOf(
        Pair(listOf("covfefe"), listOf("covfefe")),
        Pair(null, listOf("covfefe")),
        Pair(listOf("covfefe"), null),
        Pair(1, 1L)
      ).forEach { (subject, expected) ->
        it("fails $subject is not the same instance as $expected") {
          fails {
            expect(subject).isSameInstanceAs(expected)
          }
        }
      }

      sequenceOf(
        Pair("covfefe", "covfefe"),
        Pair(1L, 1L),
        Pair(null, null),
        listOf("covfefe").let { Pair(it, it) }
      ).forEach { (subject, expected) ->
        it("succeeds $subject is the same instance as $expected") {
          expect(subject).isSameInstanceAs(expected)
        }
      }
    }
  }
})