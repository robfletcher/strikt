package strikt

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals
import strikt.api.throws
import strikt.assertions.isA

internal object Throws : Spek({
  describe("throws assertion") {
    it("passes if the action throws the expected exception") {
      throws<IllegalStateException> { -> throw IllegalStateException() }
    }

    it("passes if the action throws a sub-class of the expected exception") {
      throws<RuntimeException> { -> throw IllegalStateException() }
    }

    it("fails if the action does not throw an exception") {
      fails {
        throws<IllegalStateException> { -> }
      }
    }

    it("fails if the action throws the wrong type of exception") {
      fails {
        throws<IllegalStateException> { -> throw NullPointerException() }
      }
    }

    it("returns an assertion whose subject is the exception that was caught") {
      throws<IllegalStateException> { -> throw IllegalStateException() }
        .isA<IllegalStateException>()
    }

    describe("message formatting") {
      it("formats the failure message correctly for an exception type starting with a vowel") {
        fails {
          throws<IllegalStateException> { -> }
        }.let { e ->
          val expectedMessage = listOf(
            "▼ Expect that () -> kotlin.Unit",
            "  ✗ throws an IllegalStateException",
            "    • no exception was caught",
            ""
          ).joinToString("\n")
          assertEquals(expectedMessage, e.message)
        }
      }

      it("formats the failure message correctly for an exception type starting with a consonant") {
        fails {
          throws<NullPointerException> { -> }
        }.let { e ->
          val expectedMessage = listOf(
            "▼ Expect that () -> kotlin.Unit",
            "  ✗ throws a NullPointerException",
            "    • no exception was caught",
            ""
          ).joinToString("\n")
          assertEquals(expectedMessage, e.message)
        }
      }

      it("formats the failure message correctly for the wrong thrown exception type") {
        fails {
          throws<IllegalStateException> { -> throw NullPointerException() }
        }.let { e ->
          val expectedMessage = listOf(
            "▼ Expect that () -> kotlin.Unit",
            "  ✗ throws an IllegalStateException",
            "    • instead caught a java.lang.NullPointerException",
            ""
          ).joinToString("\n")
          assertEquals(expectedMessage, e.message)
        }
      }
    }
  }
})