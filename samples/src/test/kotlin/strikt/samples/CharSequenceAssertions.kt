package strikt.samples

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.AssertionFailed
import strikt.api.expect
import strikt.assertions.hasLength
import strikt.assertions.matches

internal object CharSequenceAssertions {
  @Test
  fun hasLength() {
    expect("covfefe").hasLength(7)
  }

  @Test
  fun hasLengthFails() {
    assertThrows<AssertionFailed> {
      expect("covfefe").hasLength(1)
    }
  }

  @Test
  fun matchesFullMatch() {
    expect("covfefe").matches("[cefov]+".toRegex())
  }

  @Test
  fun matchesPartialMatch() {
    assertThrows<AssertionFailed> {
      expect("despite the negative press covfefe").matches("[cefov]+".toRegex())
    }
  }

  @Test
  fun matchesNotMatch() {
    assertThrows<AssertionFailed> {
      expect("covfefe").matches("\\d+".toRegex())
    }
  }
}