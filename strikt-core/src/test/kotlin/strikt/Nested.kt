package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.isLowerCase
import strikt.assertions.isNull
import strikt.assertions.isUpperCase

@DisplayName("assertions in blocks")
internal class Nested {
  @Test
  fun `all assertions in a block are evaluated even if some fail`() {
    fails {
      expect {
        that("fnord" as Any?).isNull()
        that("FNORD").isUpperCase()
        that("FNØRD").isLowerCase()
      }
    }.let { error ->
      val expected = "▼ Expect that \"fnord\":\n" +
        "  ✗ is null\n" +
        "▼ Expect that \"FNORD\":\n" +
        "  ✓ is upper case\n" +
        "▼ Expect that \"FNØRD\":\n" +
        "  ✗ is lower case"
      assertEquals(expected, error.message)
    }
  }
}
