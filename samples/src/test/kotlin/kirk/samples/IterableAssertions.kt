package kirk.samples

import kirk.api.AssertionFailed
import kirk.api.expect
import kirk.assertions.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
    assertThrows<AssertionFailed> {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expect(subject).all {
        startsWith('c')
      }
    }
      .let { failure ->
        assertEquals(3, failure.assertionCount, "Assertions")
        assertEquals(1, failure.passCount, "Passed")
        assertEquals(2, failure.failureCount, "Failed")
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
    assertThrows<AssertionFailed> {
      val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
      expect(subject).any {
        isLowerCase()
      }
    }
      .let { failure ->
        assertEquals(3, failure.assertionCount, "Assertions")
        assertEquals(0, failure.passCount, "Passed")
        assertEquals(3, failure.failureCount, "Failed")
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
    assertThrows<AssertionFailed> {
      val subject = setOf("catflap", "RUBBERPLANT", "MARZIPAN")
      expect(subject).none {
        isUpperCase()
      }
    }
      .let { failure ->
        assertEquals(3, failure.assertionCount, "Assertions")
        assertEquals(2, failure.passCount, "Passed")
        assertEquals(1, failure.failureCount, "Failed")
      }
  }

  @Test
  fun noneWithAllMatchingElements() {
    assertThrows<AssertionFailed> {
      val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
      expect(subject).none {
        isUpperCase()
      }
    }
      .let { failure ->
        assertEquals(3, failure.assertionCount, "Assertions")
        assertEquals(3, failure.passCount, "Passed")
        assertEquals(0, failure.failureCount, "Failed")
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
    assertThrows<AssertionFailed> {
      expect(listOf("catflap", "rubberplant", "marzipan")).contains("covfefe")
    }
  }

  @Test
  fun containsWithSomeMissingElements() {
    assertThrows<AssertionFailed> {
      expect(listOf("catflap", "rubberplant", "marzipan")).contains("covfefe", "marzipan", "bojack")
    }
      .let { e ->
        assertEquals(3, e.assertionCount, "Assertions")
        assertEquals(1, e.passCount, "Passed")
        assertEquals(2, e.failureCount, "Failed")
      }
  }

  @Test
  fun containsWithNoExpectedElements() {
    assertThrows<AssertionFailed> {
      expect(listOf("catflap", "rubberplant", "marzipan")).contains()
    }
  }

  @Test
  fun containsOnEmptySubject() {
    assertThrows<AssertionFailed> {
      expect(emptyList<String>()).contains("catflap")
    }
  }
}