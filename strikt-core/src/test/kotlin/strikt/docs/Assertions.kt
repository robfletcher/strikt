package strikt.docs

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import strikt.api.catching
import strikt.api.expect
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.all
import strikt.assertions.hasLength
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isLessThan
import strikt.assertions.isLowerCase
import strikt.assertions.isNull
import strikt.assertions.isUpperCase
import strikt.assertions.startsWith
import strikt.assertions.throws
import strikt.internal.opentest4j.CompoundAssertionFailure

@DisplayName("Snippets used in Orchid docs")
internal class Assertions {

// assertion-styles.md
// -----------------------------------------------------------------------------

  @Test fun `assertion styles 1, 2`() {
    // START assertion_styles_2
    val s = """ // IGNORE
    ▼ Expect that "fnord":
      ✓ is an instance of java.lang.String
      ✗ has length 1 : found 5
    """ // IGNORE
    // END assertion_styles_2

    expectThat(catching {
      // START assertion_styles_1
      val subject = "fnord"
      expectThat(subject)
        .isA<String>()
        .hasLength(1)
        .isUpperCase()
      // END assertion_styles_1
    }).throws<AssertionFailedError>()
      .get { message }
      .isEqualTo(s.replace(" // IGNORE", "").trimIndent().trim())
  }

  @Test fun `assertion styles 3, 4`() {
    // START assertion_styles_4
    val s = """ // IGNORE
    ▼ Expect that "fnord":
      ✓ is an instance of java.lang.String
      ✗ has length 1 : found 5
      ✗ is upper case
    """ // IGNORE
    // END assertion_styles_4

    expectThat(catching {
      // START assertion_styles_3
      val subject = "fnord"
      expectThat(subject) {
        isA<String>()
        hasLength(1)
        isUpperCase()
      }
      // END assertion_styles_3
    }).throws<CompoundAssertionFailure>()
      .get { message }
      .isEqualTo(s.replace(" // IGNORE", "").trimIndent().trim())
  }

  @Test fun `assertion styles 5, 6`() {
    // START assertion_styles_6
    val s = """ // IGNORE
    ▼ Expect that 1:
      ✗ is less than 1
      ✗ is greater than 1
    """ // IGNORE
    // END assertion_styles_6

    expectThat(catching {
      // START assertion_styles_5
      val subject = 1L
      expectThat(subject) {
        isLessThan(1L).isA<Int>()
        isGreaterThan(1L)
      }
      // END assertion_styles_5
    }).throws<CompoundAssertionFailure>()
      .get { message }
      .isEqualTo(s.replace(" // IGNORE", "").trimIndent().trim())
  }

  @Test fun `assertion styles 7, 8`() {
    // START assertion_styles_8
    val s = """ // IGNORE
    ▼ Expect that "fnord":
      ✓ is an instance of java.lang.String
      ✗ has length 1 : found 5
      ✗ is upper case
    ▼ Expect that 1:
      ✗ is less than 1
      ✗ is an instance of java.lang.Integer : found java.lang.Long
      ✗ is greater than 1
    """ // IGNORE
    // END assertion_styles_8

    expectThat(catching {
      // START assertion_styles_7
      expect {
        that("fnord")
          .isA<String>()
          .hasLength(1)
          .isUpperCase()
        that(1L) {
          isLessThan(1L).isA<Int>()
          isGreaterThan(1L)
        }
      }
      // END assertion_styles_7
    }).throws<CompoundAssertionFailure>()
      .get { message }
      .isEqualTo(s.replace(" // IGNORE", "").trimIndent().trim())
  }

// collection-elements.md
// -----------------------------------------------------------------------------

  @Test fun `collections 1, 2`() {
    // START collections_1
    val s = """ // IGNORE
    ▼ Expect that ["catflap", "rubberplant", "marzipan"]:
      ✗ all elements match:
        ▼ "catflap":
          ✓ is lower case
          ✓ starts with 'c'
        ▼ "rubberplant":
          ✓ is lower case
          ✗ starts with 'c' : found 'r'
        ▼ "marzipan":
          ✓ is lower case
          ✗ starts with 'c' : found 'm'
    """ // IGNORE
    // END collections_1

    expectThat(catching {
      // START collections_2
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expectThat(subject).all {
        isLowerCase()
        startsWith('c')
      }
      // END collections_2
    }).throws<AssertionFailedError>()
      .get { message }
      .isEqualTo(s.replace(" // IGNORE", "").trimIndent().trim())
  }

// expecting-exceptions.md
// -----------------------------------------------------------------------------

  @Test fun `catching exceptions 1, 2`() {
    // START catching_exceptions_1
    expectThat(catching { identifyHotdog("hamburger") })
      .throws<NotHotdogException>()
    // END catching_exceptions_1

    // START catching_exceptions_2
    expectThat(catching { identifyHotdog("hotdog") })
      .isNull()
    // END catching_exceptions_2
  }

  @Test fun `expect_throws 1`() {
    // START expect_throws_1
    expectThrows<NotHotdogException> {
      identifyHotdog("hamburger")
    }
    // END expect_throws_1
  }

  private fun identifyHotdog(food: String) {
    if (food != "hotdog") throw NotHotdogException()
  }

  private class NotHotdogException : Exception()

// flow-typing.md
// -----------------------------------------------------------------------------

  @Test fun `flow typing 1`() {
    // START flow_typing_1
    val subject: Map<String, Any> = mapOf("count" to 1, "name" to "Rob")
    expectThat(subject["count"])
      .isA<Int>()
      .isGreaterThan(0)

    expectThat(subject["name"])
      .isA<String>()
      .hasLength(3)
    // END flow_typing_1
  }
}
