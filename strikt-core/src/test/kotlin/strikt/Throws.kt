package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expect
import strikt.api.throws
import strikt.assertions.isA
import strikt.assertions.throws

@DisplayName("throws assertion")
internal class Throws {
  @Test
  fun `throws passes if the action throws the expected exception`() {
    throws<IllegalStateException> { -> throw IllegalStateException() }
  }

  @Test
  fun `throws passes if the action throws a sub-class of the expected exception`() {
    throws<RuntimeException> { -> throw IllegalStateException() }
  }

  @Test
  fun `throws fails if the action does not throw any exception`() {
    fails {
      throws<IllegalStateException> { -> }
    }.let { e ->
      val expected = "▼ Expect that () -> kotlin.Unit:\n" +
        "  ✗ throws java.lang.IllegalStateException : nothing was thrown"
      assertEquals(expected, e.message)
    }
  }

  @Test
  fun `throws fails if the action throws the wrong type of exception`() {
    assertThrows<AssertionFailedError> {
      throws<IllegalStateException> { -> throw NullPointerException() }
    }.let { e ->
      val expected = "▼ Expect that () -> kotlin.Unit:\n" +
        "  ✗ throws java.lang.IllegalStateException : java.lang.NullPointerException was thrown"
      assertEquals(expected, e.message)
      assertEquals(NullPointerException::class.java, e.cause?.javaClass)
    }
  }

  @Test
  fun `throws returns an assertion whose subject is the exception that was caught`() {
    throws<IllegalStateException> { -> throw IllegalStateException() }
      .isA<IllegalStateException>()
  }

  @Test
  fun `throws formats the message for a callable reference`() {
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
      val expected = "▼ Expect that MyThing::throwSomething:\n" +
        "  ✗ throws java.lang.IllegalStateException : java.lang.NullPointerException was thrown"
      assertEquals(expected, e.message)
    }
  }

  @Test
  fun `expect - throws works with blocks that don't return unit`() {
    fails {
      expect {
        @Suppress("UNUSED_EXPRESSION")
        "String"
      }.throws<IllegalStateException>()
    }
  }
}
