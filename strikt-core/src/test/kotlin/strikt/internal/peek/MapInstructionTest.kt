package strikt.internal.peek

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MapInstructionTest {
  private val niceInstruction =
    ParsedMapInstruction("""get { name }.isEqualTo("Ziggy")""")

  @Test
  fun `knows the body`() {
    Assertions.assertEquals("name", niceInstruction.body)
  }
}
