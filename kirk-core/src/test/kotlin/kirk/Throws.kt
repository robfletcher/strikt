package kirk

import kirk.api.throws
import kirk.assertions.isA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

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
  }
})