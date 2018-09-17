package strikt.internal

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.opentest4j.AssertionFailedError
import strikt.api.catching
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.message
import strikt.assertions.throws

internal class NegatedPhraseTest {
  @TestFactory
  fun `can negate assertion phrasing`() =
    listOf(
      "uncommon assertion phrasing" to "does not match: uncommon assertion phrasing",
      "is equal to %s" to "is not equal to \"foo\"",
      "contains %s" to "does not contain \"foo\"",
      "has size %s" to "does not have size \"foo\"",
      "is not null" to "is null",
      "starts with %s" to "does not start with \"foo\"",
      "ends with %s" to "does not end with \"foo\"",
      "matches the regex %s" to "does not match the regex \"foo\"",
      "throws %s" to "does not throw \"foo\""
    )
      .map { (phrase, expected) ->
        dynamicTest("\"$phrase\" is negated to \"$expected\"") {
          expectThat(catching {
            expectThat("fnord").not().assert(phrase, "foo") { pass() }
          }).throws<AssertionFailedError>()
            .message
            .isEqualTo("▼ Expect that \"fnord\":\n  ✗ $expected")
        }
      }
}
