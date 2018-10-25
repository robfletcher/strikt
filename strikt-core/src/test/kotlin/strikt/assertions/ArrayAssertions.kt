package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.internal.reporting.toHex
import java.lang.System.nanoTime
import java.util.Random

@DisplayName("assertions on array types")
internal object ArrayAssertions {

  @TestFactory
  fun `byte arrays`() = assertionTests<ByteArray> {
    randomBytes().let { subject ->
      fixture { expectThat(subject) }

      context("contentEquals") {
        test("contents are equal to a copy of itself") {
          contentEquals(subject.copyOf())
        }

        test("contents are not equal to a different array") {
          val expected = randomBytes()
          val error = assertThrows<AssertionError> {
            contentEquals(expected)
          }
          assertEquals(
            "▼ Expect that 0x${subject.toHex()}:\n" +
              "  ✗ array content equals 0x${expected.toHex()}",
            error.message
          )
        }

        test("contents are not equal to a sub-array") {
          val expected = subject.copyOf(subject.size / 2)
          val error = assertThrows<AssertionError> {
            contentEquals(expected)
          }
          assertEquals(
            "▼ Expect that 0x${subject.toHex()}:\n" +
              "  ✗ array content equals 0x${expected.toHex()}",
            error.message
          )
        }
      }
    }
  }

  @Test
  fun `can map an array to a list for easier matching`() {
    val array = arrayOf("catflap", "rubberplant", "marzipan")
    expectThat(array).toList().first().isEqualTo("catflap")
  }
}

internal fun randomBytes(): ByteArray =
  ByteArray(8).also(Random(nanoTime())::nextBytes)
