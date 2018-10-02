package strikt.internal.peek

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MapInstructionTest {
  private val niceInstruction =
    ParsedMapInstruction("""get { it.name }.isEqualTo("Ziggy")""")

  @Test
  fun `knows the body`() {
    Assertions.assertEquals("it.name", niceInstruction.body)
  }
}
