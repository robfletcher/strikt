package strikt.internal.peek

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParsedAssertInstructionTest {
  private val niceInstruction =
    ParsedAssertInstruction("""that(userId.toUpperCase()).equals("12".toLowerCase())""")
  private val instructionWithRandomWhitespace =
    ParsedAssertInstruction("""  that(  userId.toUpperCase()  ).   equals   ("12".toLowerCase())""")

  @Test
  fun `knows the subject`() {
    Assertions.assertEquals("userId.toUpperCase()", niceInstruction.subject)
    Assertions
      .assertEquals(
        "userId.toUpperCase()",
        instructionWithRandomWhitespace.subject
      )
  }

  @Test
  fun `knows the called method`() {
    Assertions.assertEquals("equals", niceInstruction.methodName)
    Assertions
      .assertEquals("equals", instructionWithRandomWhitespace.methodName)
  }

  @Test
  fun `knows the method parameter`() {
    Assertions
      .assertEquals("\"12\".toLowerCase()", niceInstruction.methodParameter)
    Assertions
      .assertEquals(
        "\"12\".toLowerCase()",
        instructionWithRandomWhitespace.methodParameter
      )
  }

  @Test
  fun `returns the rest of the string if it's not a method call`() {
    val assert =
      ParsedAssertInstruction("""that(userId.toUpperCase()) == "12".toLowerCase()""")
    Assertions.assertEquals("userId.toUpperCase()", assert.subject)
    Assertions.assertEquals("""== "12".toLowerCase()""", assert.methodName)
    Assertions.assertEquals("", assert.methodParameter)
  }
}

