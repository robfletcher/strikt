package strikt

import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.first
import strikt.assertions.hasLength
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isUpperCase
import strikt.assertions.message
import strikt.assertions.startsWith
import strikt.internal.opentest4j.AtomicAssertionFailure
import strikt.internal.opentest4j.CompoundAssertionFailure

class Exceptions {
  @Test
  fun `chained assertions raise a compound exception`() {
    fails {
      expect("fnord").hasLength(5).isUpperCase().startsWith("f")
    }.let { error ->
      expect(error)
        .isA<CompoundAssertionFailure>()
        .and {
          message.isEqualTo(
            "▼ Expect that \"fnord\":\n" +
              "  ✓ has length 5\n" +
              "  ✗ is upper case"
          )
          map { it.failures }
            .hasSize(1)
            .first()
            .isA<AtomicAssertionFailure>()
            .message.isEqualTo(
            "▼ Expect that \"fnord\":\n" +
              "  ✗ is upper case"
          )
        }
    }
  }

  @Test
  fun `block assertions raise a compound exception`() {
    fails {
      expect("fnord") {
        hasLength(5)
        isUpperCase()
        startsWith("f")
      }
    }.let { error ->
      expect(error)
        .isA<CompoundAssertionFailure>()
        .and {
          message.isEqualTo(
            "▼ Expect that \"fnord\":\n" +
              "  ✓ has length 5\n" +
              "  ✗ is upper case\n" +
              "  ✓ starts with \"f\""
          )
          map { it.failures }
            .hasSize(1)
            .first()
            .isA<AtomicAssertionFailure>()
            .message.isEqualTo(
            "▼ Expect that \"fnord\":\n" +
              "  ✗ is upper case"
          )
        }
    }
  }

  // TODO: nested blocks only have one compound
  // TODO: atomic messages are just for the condition that failed (with sub-tree)
  // TODO: expected / actual are correctly defined / undefined
}
