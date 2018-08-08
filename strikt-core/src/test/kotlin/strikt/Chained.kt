package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isLowerCase
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isUpperCase

@DisplayName("assertions in chains")
internal class Chained {
  @Test
  fun `stops on the first failed assertion in the chain`() {
    fails {
      val subject: Any? = null
      expect(subject).isNotNull().isA<String>()
    }
  }

  @Test
  fun `not() negates assertions`() {
    fails {
      val subject: Any? = null
      expect(subject).not().isNull()
    }
  }

  @Test
  fun `not() affects the entire chain`() {
    val subject = "fnord"
    expect(subject).not().isUpperCase().isA<Int>().isEqualTo(1L)
  }

  @Test
  fun `not() affects the assertion message`() {
    val subject = "fnord"
    fails {
      expect(subject).not().isLowerCase()
    }.let { error ->
      assertEquals(
        "▼ Expect that \"fnord\":\n" +
          "  ▼ Expect that \"fnord\" does not match:\n" +
          "    ✗ is lower case",
        error.message
      )
    }
  }
}
