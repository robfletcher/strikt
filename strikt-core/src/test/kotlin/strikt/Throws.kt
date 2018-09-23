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
    expectThat(catching { error("o noes") })
      .throws<IllegalStateException>()
  }

  @Test
  fun `throws passes if the action throws a sub-class of the expected exception`() {
    expectThat(catching { error("o noes") })
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
      expectThat(catching { error("o noes") })
        .throws<NullPointerException>()
    }.let { e ->
      val expected = "▼ Expect that java.lang.IllegalStateException:\n" +
        "  ✗ threw java.lang.NullPointerException : java.lang.IllegalStateException was thrown"
      assertEquals(expected, e.message)
      assertEquals(IllegalStateException::class.java, e.cause?.javaClass)
    }
  }

  @Test
  fun `throws returns an assertion whose subject is the exception that was caught`() {
    expectThat(catching { error("o noes") })
      .throws<IllegalStateException>()
      .isA<IllegalStateException>()
  }
}
