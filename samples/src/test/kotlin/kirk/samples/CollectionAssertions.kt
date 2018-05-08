package kirk.samples

import kirk.api.AssertionFailed
import kirk.api.expect
import kirk.assertions.hasSize
import kirk.assertions.isEmpty
import kirk.assertions.isNotEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal object CollectionAssertions {
  @Test
  fun hasSize() {
    val subject = setOf("catflap", "rubberplant", "marzipan")
    expect(subject).hasSize(3)
  }

  @Test
  fun hasSizeFails() {
    assertThrows<AssertionFailed> {
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
    assertThrows<AssertionFailed> {
      expect(listOf("catflap", "rubberplant", "marzipan")).isEmpty()
    }
  }

  @Test
  fun isNotEmptyFails() {
    assertThrows<AssertionFailed> {
      expect(emptyList<Any>()).isNotEmpty()
    }
  }

  @Test
  fun isNotEmpty() {
    expect(listOf("catflap", "rubberplant", "marzipan")).isNotEmpty()
  }
}