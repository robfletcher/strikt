package strikt.samples

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.MultipleFailuresError
import strikt.api.expect
import strikt.assertions.hasLength
import strikt.assertions.matches

internal object CharSequenceAssertions {
  @Test
  fun hasLength() {
    expect("fnord").hasLength(5)
  }

  @Test
  fun hasLengthFails() {
    assertThrows<MultipleFailuresError> {
      expect("fnord").hasLength(1)
    }
  }

  @Test
  fun matchesFullMatch() {
    expect("fnord").matches("[dfnor]+".toRegex())
  }

  @Test
  fun matchesPartialMatch() {
    assertThrows<MultipleFailuresError> {
      expect("despite the negative press fnord").matches("[cefov]+".toRegex())
    }
  }

  @Test
  fun matchesNotMatch() {
    assertThrows<MultipleFailuresError> {
      expect("fnord").matches("\\d+".toRegex())
    }
  }
}
