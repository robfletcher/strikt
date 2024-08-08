package strikt

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.containsExactly
import strikt.assertions.hasLength
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
    val error =
      assertThrows<AssertionError> {
        expectThat(subject).not().isLowerCase()
      }
    expectThat(error.message).isEqualTo(
      "▼ Expect that \"fnord\":\n" +
        "  ✗ is not lower case"
    )
  }

  @Test
  fun `only throws a single exception`() {
    val error =
      assertThrows<AssertionError> {
        expectThat(listOf(1, 2, 3, 4)).containsExactly(1, 2)
      }
    val expected =
      """▼ Expect that [1, 2, 3, 4]:
        |  ✗ contains exactly the elements [1, 2]
        |    ✓ contains 1
        |    ✓ …at index 0
        |    ✓ contains 2
        |    ✓ …at index 1
        |    ✗ contains no further elements
        |      found [3, 4]
      """.trimMargin()
    expectThat(error)
      .isA<AssertionFailedError>()
      .message
      .isEqualTo(expected)
  }

  @Suppress("RedundantNullableReturnType")
  @Test
  fun `can connect a block to a chain with and`() {
    val error =
      assertThrows<AssertionError> {
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
    expectThat(error.message).isEqualTo(
      """▼ Expect that "fnord":
        |  ✓ is not null
        |  ✓ is lower case
        |  ✓ contains "f"
        |  ✓ contains "n"
        |  ✗ contains "z"
        |       found "fnord""""
        .trimMargin()
    )
  }

  @Test
  fun `and { } and { }`() {
    assertThrows<AssertionError> {
      expectThat("one")
        .and {
          isEqualTo("one")
        }
        .and {
          isEqualTo("two")
        }
    }
  }

  @Test
  fun `and { } assert()`() {
    assertThrows<AssertionError> {
      expectThat("one")
        .and {
          isEqualTo("one")
        }
        .isEqualTo("two")
    }
  }

  @Test
  fun `assert() and { }`() {
    assertThrows<AssertionError> {
      expectThat("one")
        .isEqualTo("one")
        .and {
          isEqualTo("two")
        }
    }
  }

  @Test
  fun `assert() assert()`() {
    assertThrows<AssertionError> {
      expectThat("one")
        .isEqualTo("one")
        .isEqualTo("two")
    }
  }

  @Test
  fun `many nested 'and { }'`() {
    assertThrows<AssertionError> {
      expectThat("one")
        .and {
          isEqualTo("one")
          and { hasLength(3) }
        }
        .and {
          isEqualTo("two")
          and { hasLength(4) }
        }
    }
  }

  /**
   * [#194](https://github.com/robfletcher/strikt/issues/194)
   */
  @Test
  fun `compound following failing isNotNull`() {
    assertThrows<AssertionError> {
      val subject: List<String>? = null
      expectThat(subject) {
        isNotNull().contains("foo", "bar")
      }
    }
  }
}
