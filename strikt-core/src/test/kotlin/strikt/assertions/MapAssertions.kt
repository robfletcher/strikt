package strikt.assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import strikt.api.expect
import strikt.fails

internal object MapAssertions : Spek({
  describe("Assertions on ${Map::class.simpleName}") {
    describe("isEmpty assertion") {
      it("passes if the subject is empty") {
        val subject = emptyMap<Any, Any>()
        expect(subject).isEmpty()
      }
      it("fails if the subject is not empty") {
        fails {
          val subject = mapOf("Eris" to "Strife and confusion")
          expect(subject).isEmpty()
        }
      }
    }

    describe("hasEntry assertion") {
      it("passes if the subject has a matching key") {
        val subject = mapOf("foo" to "bar")
        expect(subject).hasEntry("foo")
      }
      it("fails if the subject does not have a matching key") {
        fails {
          val subject = emptyMap<Any, Any>()
          expect(subject).hasEntry("foo")
        }.let { e ->
          assertEquals(
            "Expect that {} (1 failure)\n" +
              "\thas an entry with the key \"foo\"",
            e.message
          )
        }
      }
      it("can chain assertions about the value") {
        fails {
          val subject = mapOf("foo" to "bar")
          expect(subject).hasEntry("foo").isEqualTo("baz")
        }.let { e ->
          assertEquals(
            "Expect that {foo=bar} (1 failure)\n" +
              "\tentry [foo] \"bar\" (1 failure)\n" +
              "\tis equal to \"baz\" : found \"bar\"",
            e.message
          )
        }
      }
    }
  }
})
