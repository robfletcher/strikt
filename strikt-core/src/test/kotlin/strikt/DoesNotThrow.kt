package strikt

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectDoesNotThrow
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@DisplayName("does not throw assertion")
internal class DoesNotThrow {
  @Test
  fun `does not throw passes if the action does not throw exception`() {
    expectDoesNotThrow { throwOrString(shouldThrow = false) }
  }

  @Test
  fun `does not throw fails if the action throws any exception`() {
    assertThrows<AssertionError> {
      expectDoesNotThrow { throwOrString(shouldThrow = true) }
    }.let { error ->
      val expected =
        """▼ Expect that Failure(java.lang.IllegalStateException: o noes):
              |  ✗ is success
              |    threw java.lang.IllegalStateException"""
          .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  @Test
  fun `does not throw returns an assertion whose subject is the result of the action`() {
    expectDoesNotThrow { throwOrString(shouldThrow = false) }
      .isA<String>()
  }

  @Test
  fun `does not throw accepts a suspending lambda`() {
    expectDoesNotThrow { delayedOutput("output") }
      .isA<String>()
  }
}

private fun throwOrString(shouldThrow: Boolean = false) = when {
  shouldThrow -> throw IllegalStateException("o noes")
  else -> "All Good"
}

private suspend fun <T> delayedOutput(output: T) =
  withContext(Dispatchers.Default) { output }
