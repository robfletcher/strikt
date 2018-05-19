package strikt.samples

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.MultipleFailuresError
import strikt.api.expect
import strikt.assertions.hasSize
import strikt.assertions.isEmpty
import strikt.assertions.isNotEmpty

internal object CollectionAssertions {
  @Test
  fun hasSize() {
    val subject = setOf("catflap", "rubberplant", "marzipan")
    expect(subject).hasSize(3)
  }

  @Test
  fun hasSizeFails() {
    assertThrows<MultipleFailuresError> {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expect(subject).hasSize(1)
    }
  }

  @Test
  fun isEmpty() {
    expect(emptyList<Any>()).isEmpty()
  }

  @Test
  fun isEmptyFails() {
    assertThrows<MultipleFailuresError> {
      expect(listOf("catflap", "rubberplant", "marzipan")).isEmpty()
    }
  }

  @Test
  fun isNotEmptyFails() {
    assertThrows<MultipleFailuresError> {
      expect(emptyList<Any>()).isNotEmpty()
    }
  }

  @Test
  fun isNotEmpty() {
    expect(listOf("catflap", "rubberplant", "marzipan")).isNotEmpty()
  }
}