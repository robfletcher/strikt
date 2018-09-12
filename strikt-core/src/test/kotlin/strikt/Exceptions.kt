package strikt

import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import strikt.api.expect
import strikt.assertions.containsExactly
import strikt.assertions.first
import strikt.assertions.get
import strikt.assertions.hasLength
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isGreaterThan
import strikt.assertions.isLessThan
import strikt.assertions.isNotEqualTo
import strikt.assertions.isTrue
import strikt.assertions.isUpperCase
import strikt.assertions.message
import strikt.assertions.startsWith
import strikt.internal.opentest4j.AtomicAssertionFailure
import strikt.internal.opentest4j.CompoundAssertionFailure

class Exceptions {
  @Test
  fun `chained assertions raise an atomic exception`() {
    fails {
      expect("fnord").hasLength(5).isUpperCase().startsWith("f")
    }.let { error ->
      expect(error)
        .isA<AtomicAssertionFailure>()
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

  @Test
  fun `chains involving "and" raise a single compound exception`() {
    fails {
      expect("fnord")
        .map(String::length)
        .isGreaterThan(0)
        .and {
          isEqualTo(1)
          isLessThan(2)
          isNotEqualTo(5)
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
        }
        .map { it.failures }
        .hasSize(3)
        .and {
          first()
            .isA<AtomicAssertionFailure>()
            .message
            .isEqualTo(
              "▼ Expect that \"fnord\":\n" +
                "  ▼ value of property length:\n" +
                "    ✗ is equal to 1 : found 5"
            )
        }
        .and {
          get(1)
            .isA<AtomicAssertionFailure>()
            .message
            .isEqualTo(
              "▼ Expect that \"fnord\":\n" +
                "  ▼ value of property length:\n" +
                "    ✗ is less than 2"
            )
        }
        .and {
          get(2)
            .isA<AtomicAssertionFailure>()
            .message
            .isEqualTo(
              "▼ Expect that \"fnord\":\n" +
                "  ▼ value of property length:\n" +
                "    ✗ is not equal to 5"
            )
        }
    }
  }

  @Test
  fun `blocks involving "and" raise a single compound exception`() {
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
        }
        .map { it.failures }
        .hasSize(1)
        .first()
        .isA<AtomicAssertionFailure>()
        .message
        .isEqualTo(
          "▼ Expect that \"fnord\":\n" +
            "  ▼ value of property length:\n" +
            "    ✓ is greater than 0\n" +
            "    ✗ is equal to 1 : found 5\n" +
            "    ✗ is less than 2\n" +
            "    ✗ is not equal to 5"
        )
    }
  }

  @Test
  fun `composed assertions raise an atomic exception`() {
    fails {
      expect(listOf("catflap", "rubberplant", "marzipan"))
        .containsExactly("catflap", "rubberplant")
    }
      .let { error ->
        expect(error)
          .isA<AtomicAssertionFailure>()
          .message
          .isEqualTo(
            "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
              "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\"]\n" +
              "    ✓ contains \"catflap\"\n" +
              "    ✓ …at index 0\n" +
              "    ✓ contains \"rubberplant\"\n" +
              "    ✓ …at index 1\n" +
              "    ✗ contains no further elements : found [\"marzipan\"]"
          )
      }
  }

  @Test
  fun `composed assertions in a block are grouped`() {
    fails {
      expect(listOf("catflap", "rubberplant", "marzipan")) {
        hasSize(2)
        containsExactly("catflap", "rubberplant")
      }
    }
      .let { error ->
        expect(error)
          .isA<CompoundAssertionFailure>()
          .map { it.failures }
          .hasSize(2)
          .and {
            first()
              .isA<AtomicAssertionFailure>()
              .message
              .isEqualTo(
                "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
                  "  ✗ has size 2 : found 3"
              )
            get(1)
              .isA<AtomicAssertionFailure>()
              .message
              .isEqualTo(
                "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
                  "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\"]\n" +
                  "    ✓ contains \"catflap\"\n" +
                  "    ✓ …at index 0\n" +
                  "    ✓ contains \"rubberplant\"\n" +
                  "    ✓ …at index 1\n" +
                  "    ✗ contains no further elements : found [\"marzipan\"]"
              )
          }
      }
  }

  @Test
  fun `expected and actual are undefined if a failure does not specify an actual`() {
    fails {
      expect("fnord")
        .assert("is %s", "something") { fail("o noes") }
    }.let { error ->
      expect(error)
        .isA<AssertionFailedError>()
        .and {
          map(AssertionFailedError::isExpectedDefined).isFalse()
          map(AssertionFailedError::isActualDefined).isFalse()
        }
    }
  }

  @Test
  fun `expected and actual are defined if a failure specifies an actual`() {
    fails {
      expect("fnord")
        .assert("is %s", "something") { fail("something else", "o noes") }
    }.let { error ->
      expect(error)
        .isA<AssertionFailedError>()
        .and {
          map(AssertionFailedError::isExpectedDefined).isTrue()
          map { it.expected.value }.isEqualTo("something")
          map(AssertionFailedError::isActualDefined).isTrue()
          map { it.actual.value }.isEqualTo("something else")
        }
    }
  }
}
