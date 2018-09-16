package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.catching
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.throws

@DisplayName("throws assertion")
internal class Throws {
  @Test
  fun `throws passes if the action throws the expected exception`() {
    expectThat(catching { throw IllegalStateException() })
      .throws<IllegalStateException>()
  }

  @Test
  fun `throws passes if the action throws a sub-class of the expected exception`() {
    expectThat(catching { throw IllegalStateException() })
      .throws<RuntimeException>()
  }

  @Test
  fun `throws fails if the action does not throw any exception`() {
    fails {
      expectThat(catching { })
        .throws<IllegalStateException>()
    }.let { e ->
      val expected = "▼ Expect that null:\n" +
        "  ✗ threw java.lang.IllegalStateException : nothing was thrown"
      assertEquals(expected, e.message)
    }
  }

  @Test
  fun `throws fails if the action throws the wrong type of exception`() {
    fails {
      expectThat(catching { throw NullPointerException() })
        .throws<IllegalStateException>()
    }.let { e ->
      val expected = "▼ Expect that java.lang.NullPointerException:\n" +
        "  ✗ threw java.lang.IllegalStateException : java.lang.NullPointerException was thrown"
      assertEquals(expected, e.message)
      assertEquals(NullPointerException::class.java, e.cause?.javaClass)
    }
  }

  @Test
  fun `throws returns an assertion whose subject is the exception that was caught`() {
    expectThat(catching { throw IllegalStateException() })
      .throws<IllegalStateException>()
      .isA<IllegalStateException>()
  }
}
