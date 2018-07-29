package strikt

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.isA
import strikt.assertions.isNotNull
import strikt.assertions.isNull

@DisplayName("assertions in blocks")
internal class Block {
  @Test
  fun `all assertions in a block are evaluated even if some fail`() {
    fails {
      val subject: Any? = "fnord"
      expect(subject) {
        isNull()
        isNotNull()
        isA<String>()
        isA<Number>()
      }
    }
  }
}
