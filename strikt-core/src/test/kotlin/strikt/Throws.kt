package strikt

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import strikt.api.expect
import strikt.api.throws
import strikt.assertions.isA
import strikt.assertions.throws

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
        val expected = "Expect that: () -> kotlin.Unit (1 failure)\n" +
          "\tthrows java.lang.IllegalStateException : nothing was thrown"
        assertEquals(expected, e.message)
      }
    }

    it("fails if the action throws the wrong type of exception") {
      fails {
        throws<IllegalStateException> { -> throw NullPointerException() }
      }.let { e ->
        val expected = "Expect that: () -> kotlin.Unit (1 failure)\n" +
          "\tthrows java.lang.IllegalStateException : java.lang.NullPointerException was thrown"
        assertEquals(expected, e.message)
        assertEquals(NullPointerException::class.java, e.failures.first().cause?.javaClass)
      }
    }

    it("returns an assertion whose subject is the exception that was caught") {
      throws<IllegalStateException> { -> throw IllegalStateException() }
        .isA<IllegalStateException>()
    }

    it("formats the message for a callable reference") {
      class Thing {
        fun throwSomething() {
          throw NullPointerException()
        }

        override fun toString(): String = "MyThing"
      }
      fails {
        val subject = Thing()
        val fn: () -> Unit = subject::throwSomething
        expect(fn).throws<IllegalStateException>()
      }.let { e ->
        val expected = "Expect that: MyThing::throwSomething (1 failure)\n" +
          "\tthrows java.lang.IllegalStateException : java.lang.NullPointerException was thrown"
        assertEquals(expected, e.message)
      }
    }
  }
})
