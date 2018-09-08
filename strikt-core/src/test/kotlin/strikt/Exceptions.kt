package strikt

import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.first
import strikt.assertions.get
import strikt.assertions.hasLength
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isLessThan
import strikt.assertions.isNotEqualTo
import strikt.assertions.isUpperCase
import strikt.assertions.message
import strikt.assertions.startsWith
import strikt.internal.opentest4j.AtomicAssertionFailure
import strikt.internal.opentest4j.CompoundAssertionFailure
import strikt.internal.opentest4j.SingleAssertionFailure

class Exceptions {
  @Test
  fun `chained assertions raise a single exception`() {
    fails {
      expect("fnord").hasLength(5).isUpperCase().startsWith("f")
    }.let { error ->
      expect(error)
        .isA<SingleAssertionFailure>()
        .and {
          message.isEqualTo(
            "▼ Expect that \"fnord\":\n" +
              "  ✓ has length 5\n" +
              "  ✗ is upper case"
          )
        }
    }
  }

  @Test
  fun `block assertions with multiple failures raise a compound exception`() {
    fails {
      expect("fnord") {
        hasLength(4)
        isUpperCase()
        startsWith("f")
      }
    }.let { error ->
      expect(error)
        .isA<CompoundAssertionFailure>()
        .and {
          message.isEqualTo(
            "▼ Expect that \"fnord\":\n" +
              "  ✗ has length 4 : found 5\n" +
              "  ✗ is upper case\n" +
              "  ✓ starts with \"f\""
          )
          map { it.failures }
            .hasSize(2)
            .first()
            .isA<AtomicAssertionFailure>()
            .message.isEqualTo(
            "▼ Expect that \"fnord\":\n" +
              "  ✗ has length 4 : found 5"
          )
        }
    }
  }

  @Test
  fun `block assertions with a single failure raise an atomic exception`() {
    fails {
      expect("fnord") {
        hasLength(5)
        isUpperCase()
        startsWith("f")
      }
    }.let { error ->
      expect(error)
        .isA<SingleAssertionFailure>()
        .message
        .isEqualTo(
          "▼ Expect that \"fnord\":\n" +
            "  ✓ has length 5\n" +
            "  ✗ is upper case\n" +
            "  ✓ starts with \"f\""
        )
    }
  }

  @Test
  fun `nested assertions raise only a single compound exception`() {
    fails {
      expect("fnord") {
        map(String::length)
          .isGreaterThan(0)
          .and {
            isEqualTo(1)
            isLessThan(2)
            isNotEqualTo(5)
          }
      }
    }.let { error ->
      expect(error)
        .isA<CompoundAssertionFailure>()
        .and {
          message.isEqualTo(
            "▼ Expect that \"fnord\":\n" +
              "  ▼ value of property length:\n" +
              "    ✓ is greater than 0\n" +
              "    ✗ is equal to 1 : found 5\n" +
              "    ✗ is less than 2\n" +
              "    ✗ is not equal to 5"
          )
          map { it.failures }
            .hasSize(3)
            .and {
              first()
                .isA<AtomicAssertionFailure>()
                .message.isEqualTo(
                "▼ Expect that \"fnord\":\n" +
                  "  ▼ value of property length:\n" +
                  "    ✗ is equal to 1 : found 5"
              )
            }
            .and {
              get(1)
                .isA<AtomicAssertionFailure>()
                .message.isEqualTo(
                "▼ Expect that \"fnord\":\n" +
                  "  ▼ value of property length:\n" +
                  "    ✗ is less than 2"
              )
            }
            .and {
              get(2)
                .isA<AtomicAssertionFailure>()
                .message.isEqualTo(
                "▼ Expect that \"fnord\":\n" +
                  "  ▼ value of property length:\n" +
                  "    ✗ is not equal to 5"
              )
            }
        }
    }
  }

  // TODO: expected / actual are correctly defined / undefined
}
