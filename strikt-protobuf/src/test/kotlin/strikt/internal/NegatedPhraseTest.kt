package strikt.internal

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.opentest4j.AssertionFailedError
import strikt.api.expect
import strikt.api.throws
import strikt.assertions.isEqualTo
import strikt.assertions.message

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
          throws<AssertionFailedError> {
            expect("fnord").not().assert(phrase, "foo") { pass() }
          }
            .message
            .isEqualTo("▼ Expect that \"fnord\":\n  ✗ $expected")
        }
      }
}
