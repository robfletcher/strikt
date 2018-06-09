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
      expect("o hai") {
        isEqualTo("kthxbye")
        isEqualTo("o HAi")
      }
    }.let {
      assertEquals(
        "Expect that: \"o hai\" (2 failures)\n" +
          "\tis equal to \"kthxbye\" : found \"o hai\"\n" +
          "\tis equal to \"o HAi\" : found \"o hai\"",
        it.message
      )
      assertEquals(2, it.failures.size)
      assertEquals("is equal to \"kthxbye\" : found \"o hai\"", it.failures[0].message)
      assertEquals("is equal to \"o HAi\" : found \"o hai\"", it.failures[1].message)
    }
  }

  @Test
  fun formatsChainDiffInIntelliJ() {
    assertThrows<MultipleFailuresError> {
      expect("o hai").isEqualTo("o HAi")
    }.let {
      assertEquals(
        "Expect that: \"o hai\" (1 failure)\n" +
          "\tis equal to \"o HAi\" : found \"o hai\"",
        it.message
      )
      assertEquals(1, it.failures.size)
      assertEquals("is equal to \"o HAi\" : found \"o hai\"", it.failures[0].message)
    }
  }
}
