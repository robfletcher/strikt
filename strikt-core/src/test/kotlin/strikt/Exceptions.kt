package strikt

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
import strikt.assertions.length
import strikt.assertions.message
import strikt.assertions.startsWith
import strikt.internal.opentest4j.CompoundAssertionFailure

class Exceptions {
  @Test
  fun `chained assertions raise an atomic exception`() {
    assertThrows<AssertionError> {
      expectThat("fnord").hasLength(5).isUpperCase().startsWith("f")
    }.let { error ->
      expectThat(error)
        .isA<AssertionFailedError>()
        .message
        .isEqualTo(
          """▼ Expect that "fnord":
            |  ✓ has length 5
            |  ✗ is upper case"""
            .trimMargin()
        )
    }
  }

  @Test
  fun `block assertions raise a compound exception`() {
    assertThrows<AssertionError> {
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
            """▼ Expect that "fnord":
              |  ✓ has length 5
              |  ✗ is upper case
              |  ✓ starts with "f""""
              .trimMargin()
          )
          get { failures }
            .hasSize(1)
            .first()
            .isA<AssertionFailedError>()
            .message.isEqualTo(
            """▼ Expect that "fnord":
              |  ✗ is upper case"""
              .trimMargin()
          )
        }
    }
  }

  @Test
  fun `chains involving "and" raise a single compound exception`() {
    assertThrows<AssertionError> {
      expectThat("fnord")
        .length
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
            """▼ Expect that "fnord":
              |  ▼ value of property length:
              |    ✓ is greater than 0
              |    ✗ is equal to 1
              |            found 5
              |    ✗ is less than 2
              |    ✗ is not equal to 5"""
              .trimMargin()
          )
        }
        .get { failures }
        .hasSize(3)
        .and {
          first()
            .isA<AssertionFailedError>()
            .message
            .isEqualTo(
              """▼ Expect that "fnord":
                |  ▼ value of property length:
                |    ✗ is equal to 1
                |            found 5"""
                .trimMargin()
            )
        }
        .and {
          get(1)
            .isA<AssertionFailedError>()
            .message
            .isEqualTo(
              """▼ Expect that "fnord":
                |  ▼ value of property length:
                |    ✗ is less than 2"""
                .trimMargin()
            )
        }
        .and {
          get(2)
            .isA<AssertionFailedError>()
            .message
            .isEqualTo(
              """▼ Expect that "fnord":
                |  ▼ value of property length:
                |    ✗ is not equal to 5"""
                .trimMargin()
            )
        }
    }
  }

  @Test
  fun `blocks involving "and" raise a single compound exception`() {
    assertThrows<AssertionError> {
      expectThat("fnord") {
        length
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
            """▼ Expect that "fnord":
              |  ▼ value of property length:
              |    ✓ is greater than 0
              |    ✗ is equal to 1
              |            found 5
              |    ✗ is less than 2
              |    ✗ is not equal to 5"""
              .trimMargin()
          )
        }
        .get { failures }
        .hasSize(1)
        .first()
        .isA<AssertionFailedError>()
        .message
        .isEqualTo(
          """▼ Expect that "fnord":
            |  ▼ value of property length:
            |    ✓ is greater than 0
            |    ✗ is equal to 1
            |            found 5
            |    ✗ is less than 2
            |    ✗ is not equal to 5"""
            .trimMargin()
        )
    }
  }

  @Test
  fun `composed assertions raise an atomic exception`() {
    assertThrows<AssertionError> {
      expectThat(listOf("catflap", "rubberplant", "marzipan"))
        .containsExactly("catflap", "rubberplant")
    }
      .let { error ->
        expectThat(error)
          .isA<AssertionFailedError>()
          .message
          .isEqualTo(
            """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
              |  ✗ contains exactly the elements ["catflap", "rubberplant"]
              |    ✓ contains "catflap"
              |    ✓ …at index 0
              |    ✓ contains "rubberplant"
              |    ✓ …at index 1
              |    ✗ contains no further elements
              |      found ["marzipan"]"""
              .trimMargin()
          )
      }
  }

  @Test
  fun `composed assertions in a block are grouped`() {
    assertThrows<AssertionError> {
      expectThat(listOf("catflap", "rubberplant", "marzipan")) {
        hasSize(2)
        containsExactly("catflap", "rubberplant")
      }
    }
      .let { error ->
        expectThat(error)
          .isA<CompoundAssertionFailure>()
          .get { failures }
          .hasSize(2)
          .and {
            first()
              .isA<AssertionFailedError>()
              .message
              .isEqualTo(
                """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
                  |  ✗ has size 2
                  |       found 3"""
                  .trimMargin()
              )
            get(1)
              .isA<AssertionFailedError>()
              .message
              .isEqualTo(
                """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
                  |  ✗ contains exactly the elements ["catflap", "rubberplant"]
                  |    ✓ contains "catflap"
                  |    ✓ …at index 0
                  |    ✓ contains "rubberplant"
                  |    ✓ …at index 1
                  |    ✗ contains no further elements
                  |      found ["marzipan"]"""
                  .trimMargin()
              )
          }
      }
  }

  @Test
  fun `expected and actual are undefined if a failure does not specify an actual`() {
    assertThrows<AssertionError> {
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
    assertThrows<AssertionError> {
      expectThat("fnord")
        .assert("is %s", "something") {
          fail("something else", "o noes")
        }
    }.let { error ->
      expectThat(error)
        .isA<AssertionFailedError>()
        .and {
          get(AssertionFailedError::isExpectedDefined).isTrue()
          get { expected.value }.isEqualTo("something")
          get(AssertionFailedError::isActualDefined).isTrue()
          get { actual.value }.isEqualTo("something else")
        }
    }
  }
}
