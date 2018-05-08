package kirk.samples

import kirk.api.AssertionFailed
import kirk.api.expect
import kirk.assertions.hasLength
import kirk.assertions.matches
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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