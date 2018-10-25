package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.containsExactly
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isLowerCase
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isUpperCase
import strikt.assertions.message

@DisplayName("assertions in chains")
internal class Chained {
  @Test
  fun `stops on the first failed assertion in the chain`() {
    assertThrows<AssertionError> {
      val subject: Any? = null
      expectThat(subject).isNotNull().isEqualTo(null)
    }
  }

  @Test
  fun `not() negates assertions`() {
    assertThrows<AssertionError> {
      val subject: Any? = null
      expectThat(subject).not().isNull()
    }
  }

  @Test
  fun `not() affects the entire chain`() {
    val subject = "fnord"
    expectThat(subject).not().isUpperCase().isA<Int>().isEqualTo(1)
  }

  @Test
  fun `not() affects the assertion message`() {
    val subject = "fnord"
    val error = assertThrows<AssertionError> {
      expectThat(subject).not().isLowerCase()
    }
    assertEquals(
      "▼ Expect that \"fnord\":\n" +
        "  ✗ is not lower case",
      error.message
    )
  }

  @Test
  fun `only throws a single exception`() {
    val error = assertThrows<AssertionError> {
      expectThat(listOf(1, 2, 3, 4)).containsExactly(1, 2)
    }
    val expected = "▼ Expect that [1, 2, 3, 4]:\n" +
      "  ✗ contains exactly the elements [1, 2]\n" +
      "    ✓ contains 1\n" +
      "    ✓ …at index 0\n" +
      "    ✓ contains 2\n" +
      "    ✓ …at index 1\n" +
      "    ✗ contains no further elements : found [3, 4]"
    expectThat(error)
      .isA<AssertionFailedError>()
      .message
      .isEqualTo(expected)
  }

  @Test
  fun `can connect a block to a chain with and`() {
    val error = assertThrows<AssertionError> {
      val subject: String? = "fnord"
      expectThat(subject)
        .isNotNull()
        .and {
          isLowerCase()
          contains("f")
          contains("n")
          contains("z")
        }
    }
    assertEquals(
      "▼ Expect that \"fnord\":\n" +
        "  ✓ is not null\n" +
        "  ✓ is lower case\n" +
        "  ✓ contains \"f\"\n" +
        "  ✓ contains \"n\"\n" +
        "  ✗ contains \"z\" : found \"fnord\"",
      error.message
    )
  }
}
