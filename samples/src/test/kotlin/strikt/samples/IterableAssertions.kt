package strikt.samples

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.MultipleFailuresError
import strikt.api.expect
import strikt.assertions.*

internal object IterableAssertions {

  @Test
  fun allWithAllMatchingElements() {
    val subject = setOf("catflap", "rubberplant", "marzipan")
    expect(subject).all {
      isLowerCase()
    }
  }

  @Test
  fun allWithNonMatchingElements() {
    assertThrows<MultipleFailuresError> {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expect(subject).all {
        startsWith('c')
      }
    }
  }

  @Test
  fun anyWithAllMatchingElements() {
    val subject = setOf("catflap", "rubberplant", "marzipan")
    expect(subject).any {
      isLowerCase()
    }
  }

  @Test
  fun anyWithSomeNonMatchingElements() {
    val subject = setOf("catflap", "RUBBERPLANT", "MARZIPAN")
    expect(subject).any {
      isLowerCase()
    }
  }

  @Test
  fun anyWithNoMatchingElements() {
    assertThrows<MultipleFailuresError> {
      val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
      expect(subject).any {
        isLowerCase()
      }
    }
  }

  @Test
  fun noneWithNoMatchingElements() {
    val subject = setOf("catflap", "rubberplant", "marzipan")
    expect(subject).none {
      isUpperCase()
    }
  }

  @Test
  fun noneWithMatchingElements() {
    assertThrows<MultipleFailuresError> {
      val subject = setOf("catflap", "RUBBERPLANT", "MARZIPAN")
      expect(subject).none {
        isUpperCase()
      }
    }
  }

  @Test
  fun noneWithAllMatchingElements() {
    assertThrows<MultipleFailuresError> {
      val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
      expect(subject).none {
        isUpperCase()
      }
    }
  }

  @Test
  fun containsWithOnlyElement() {
    expect(listOf("catflap")).contains("catflap")
  }

  @Test
  fun containsWithExpectedElement() {
    expect(listOf("catflap", "rubberplant", "marzipan")).contains("catflap")
  }

  @Test
  fun containsWithMultipleExpectedElements() {
    expect(listOf("catflap", "rubberplant", "marzipan")).contains("catflap", "marzipan")
  }

  @Test
  fun containsWithMissingElement() {
    assertThrows<MultipleFailuresError> {
      expect(listOf("catflap", "rubberplant", "marzipan")).contains("covfefe")
    }
  }

  @Test
  fun containsWithSomeMissingElements() {
    assertThrows<MultipleFailuresError> {
      expect(listOf("catflap", "rubberplant", "marzipan")).contains("covfefe", "marzipan", "bojack")
    }
  }

  @Test
  fun containsWithNoExpectedElements() {
    assertThrows<MultipleFailuresError> {
      expect(listOf("catflap", "rubberplant", "marzipan")).contains()
    }
  }

  @Test
  fun containsOnEmptySubject() {
    assertThrows<MultipleFailuresError> {
      expect(emptyList<String>()).contains("catflap")
    }
  }
}