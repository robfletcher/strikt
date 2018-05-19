package strikt.samples

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.MultipleFailuresError
import strikt.api.expect
import strikt.assertions.isEqualTo

internal object DiffFormatting {

  @Test
  fun formatsBlockDiffInIntelliJ() {
    assertThrows<MultipleFailuresError> {
      expect("Expect that: %s", "o hai") {
        isEqualTo("kthxbye")
        isEqualTo("o HAi")
      }
    }.let {
      assertEquals(2, it.failures.size)
      assertEquals("\"o hai\" is equal to \"kthxbye\"", it.failures[0].message)
      assertEquals("\"o hai\" is equal to \"o HAI\"", it.failures[1].message)
    }
  }

  @Test
  fun formatsChainDiffInIntelliJ() {
    assertThrows<MultipleFailuresError> {
      expect("o hai").isEqualTo("o HAi")
    }.let {
      assertEquals(1, it.failures.size)
      assertEquals("\"o hai\" is equal to \"o HAi\"", it.failures[0].message)
    }
  }
}