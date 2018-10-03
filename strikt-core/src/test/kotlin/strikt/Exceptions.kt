package strikt

import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
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
import strikt.internal.opentest4j.CompoundAssertionFailure

class Exceptions {
  @Test
  fun `chained assertions raise an atomic exception`() {
    fails {
      expectThat("fnord").hasLength(5).isUpperCase().startsWith("f")
    }.let { error ->
      expectThat(error)
        .isA<AssertionFailedError>()
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
      expectThat("fnord") {
        hasLength(5)
        isUpperCase()
        startsWith("f")
      }
    }.let { error ->
      expectThat(error)
        .isA<CompoundAssertionFailure>()
        .and {
          message.isEqualTo(
            "▼ Expect that \"fnord\":\n" +
              "  ✓ has length 5\n" +
              "  ✗ is upper case\n" +
              "  ✓ starts with \"f\""
          )
          get { it.failures }
            .hasSize(1)
            .first()
            .isA<AssertionFailedError>()
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
      expectThat("fnord")
        .get(String::length)
        .isGreaterThan(0)
        .and {
          isEqualTo(1)
          isLessThan(2)
          isNotEqualTo(5)
        }
    }.let { error ->
      expectThat(error)
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
        .get { it.failures }
        .hasSize(3)
        .and {
          first()
            .isA<AssertionFailedError>()
            .message
            .isEqualTo(
              "▼ Expect that \"fnord\":\n" +
                "  ▼ value of property length:\n" +
                "    ✗ is equal to 1 : found 5"
            )
        }
        .and {
          get(1)
            .isA<AssertionFailedError>()
            .message
            .isEqualTo(
              "▼ Expect that \"fnord\":\n" +
                "  ▼ value of property length:\n" +
                "    ✗ is less than 2"
            )
        }
        .and {
          get(2)
            .isA<AssertionFailedError>()
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
      expectThat("fnord") {
        get(String::length)
          .isGreaterThan(0)
          .and {
            isEqualTo(1)
            isLessThan(2)
            isNotEqualTo(5)
          }
      }
    }.let { error ->
      expectThat(error)
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
        .get { it.failures }
        .hasSize(1)
        .first()
        .isA<AssertionFailedError>()
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
      expectThat(listOf("catflap", "rubberplant", "marzipan"))
        .containsExactly("catflap", "rubberplant")
    }
      .let { error ->
        expectThat(error)
          .isA<AssertionFailedError>()
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
      expectThat(listOf("catflap", "rubberplant", "marzipan")) {
        hasSize(2)
        containsExactly("catflap", "rubberplant")
      }
    }
      .let { error ->
        expectThat(error)
          .isA<CompoundAssertionFailure>()
          .get { it.failures }
          .hasSize(2)
          .and {
            first()
              .isA<AssertionFailedError>()
              .message
              .isEqualTo(
                "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
                  "  ✗ has size 2 : found 3"
              )
            get(1)
              .isA<AssertionFailedError>()
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
      expectThat("fnord")
        .assert("is %s", "something") {
          fail("o noes")
        }
    }.let { error ->
      expectThat(error)
        .isA<AssertionFailedError>()
        .and {
          get(AssertionFailedError::isExpectedDefined).isFalse()
          get(AssertionFailedError::isActualDefined).isFalse()
        }
    }
  }

  @Test
  fun `expected and actual are defined if a failure specifies an actual`() {
    fails {
      expectThat("fnord")
        .assert("is %s", "something") {
          fail("something else", "o noes")
        }
    }.let { error ->
      expectThat(error)
        .isA<AssertionFailedError>()
        .and {
          get(AssertionFailedError::isExpectedDefined).isTrue()
          get { it.expected.value }.isEqualTo("something")
          get(AssertionFailedError::isActualDefined).isTrue()
          get { it.actual.value }.isEqualTo("something else")
        }
    }
  }
}
