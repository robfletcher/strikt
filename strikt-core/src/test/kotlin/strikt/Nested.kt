package strikt

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isLowerCase
import strikt.assertions.isNull
import strikt.assertions.isUpperCase
import strikt.assertions.message

@DisplayName("assertions in blocks")
internal class Nested {
  @Test
  fun `all assertions in a block are evaluated even if some fail`() {
    assertThrows<AssertionError> {
      expect {
        that("fnord" as Any?).isNull()
        that("FNORD").isUpperCase()
        that("FNØRD").isLowerCase()
      }
    }.let { error ->
      val expected =
        "▼ Expect that \"fnord\":\n" +
          "  ✗ is null\n" +
          "▼ Expect that \"FNORD\":\n" +
          "  ✓ is upper case\n" +
          "▼ Expect that \"FNØRD\":\n" +
          "  ✗ is lower case"
      expectThat(expected).isEqualTo(error.message)
    }
  }

  @Test
  fun `nested expectations can be described`() {
    expectThat(
      assertThrows<AssertionError> {
        expect {
          (1 until 3).forEach {
            that("foo").describedAs("pass $it").isEqualTo("bar")
          }
        }
      }
    ).message isEqualTo
      """▼ Expect that pass 1:
                          |  ✗ is equal to "bar"
                          |          found "foo"
                          |▼ Expect that pass 2:
                          |  ✗ is equal to "bar"
                          |          found "foo"
      """.trimMargin()
  }

  @Test
  fun `nested expectations accepts a suspending lambda`() {
    expect {
      that(delayedReturnValue(6)).isEqualTo(6)
    }
  }

  @Test
  fun `all assertions in a block are evaluated even if some fail async`() {
    assertThrows<AssertionError> {
      expect {
        that(delayedReturnValue("fnord" as Any?)).isNull()
        that(delayedReturnValue("FNORD")).isUpperCase()
        that(delayedReturnValue("FNØRD")).isLowerCase()
      }
    }.let { error ->
      val expected =
        "▼ Expect that \"fnord\":\n" +
          "  ✗ is null\n" +
          "▼ Expect that \"FNORD\":\n" +
          "  ✓ is upper case\n" +
          "▼ Expect that \"FNØRD\":\n" +
          "  ✗ is lower case"
      expectThat(expected).isEqualTo(error.message)
    }
  }
}

private suspend fun <T> delayedReturnValue(input: T): T =
  withContext(Dispatchers.Default) {
    input
  }
