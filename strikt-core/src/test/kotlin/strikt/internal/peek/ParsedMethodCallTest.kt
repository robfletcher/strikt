package strikt.internal.peek

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParsedMethodCallTest {

  @Test
  fun `knows the body`() {
    Assertions.assertEquals("name",
      ParsedMethodCall("""get { name }.isEqualTo("Ziggy")""", "get").body)
  }
  @Test
  fun `works with different method names`() {
    Assertions.assertEquals("name",
      ParsedMethodCall("""callMe { name }.isEqualTo("Ziggy")""", "callMe").body)
  }
}
