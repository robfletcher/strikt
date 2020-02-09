package strikt

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.hasLength
import strikt.assertions.isEqualTo
import strikt.assertions.isLowerCase

@DisplayName("composed assertions")
internal class Composition {

  @Test
  fun `composed assertions can be negated`() {
    assertThrows<AssertionError> {
      expectThat("fnord").compose("matches a negated assertion") {
        not().isLowerCase().hasLength(5)
      } then {
        if (allPassed) pass() else fail()
      }
    }.let { error ->
      val expected = "▼ Expect that \"fnord\":\n" +
        "  ✗ matches a negated assertion\n" +
        "    ✗ is not lower case"
      expectThat(error.message).isEqualTo(expected)
    }
  }
}
