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
      }.let { e ->
        val expected = "Expect that () -> kotlin.Unit (1 failure)\n" +
          "\tthrows java.lang.IllegalStateException"
        // TODO: should indicate nothing was caught
        assertEquals(expected, e.message)
      }
    }

    it("fails if the action throws the wrong type of exception") {
      fails {
        throws<IllegalStateException> { -> throw NullPointerException() }
      }.let { e ->
        val expected = "Expect that () -> kotlin.Unit (1 failure)\n" +
          "\tthrows java.lang.IllegalStateException"
        // TODO: should indicate the actual exception type
        assertEquals(expected, e.message)
      }
    }

    it("returns an assertion whose subject is the exception that was caught") {
      throws<IllegalStateException> { -> throw IllegalStateException() }
        .isA<IllegalStateException>()
    }
  }
})