package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.hasLength
import strikt.assertions.isLowerCase

@DisplayName("composed assertions")
internal class Composition {

  @Test
  fun `composed assertions can be negated`() {
    fails {
      expect("fnord").compose("matches a negated assertion") {
        not().isLowerCase().hasLength(5)
      } then {
        if (allPassed) pass() else fail()
      }
    }.let { error ->
      val expected = "▼ Expect that \"fnord\":\n" +
        "  ✗ matches a negated assertion\n" +
        "    ✗ not is lower case\n" +
        "    ✗ not has length 5"
      assertEquals(expected, error.message)
    }
  }
}
