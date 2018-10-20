package strikt.assertions

import com.oneeyedmen.minutest.junit.junitTests
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.fails
import strikt.internal.reporting.toHex
import java.lang.System.nanoTime
import java.util.Random

@DisplayName("assertions on array types")
internal object ArrayAssertions {

  @TestFactory
  fun `byte arrays`() = junitTests<ByteArray> {
    fixture { randomBytes() }

    context("contentEquals") {
      test("contents are equal to a copy of itself") {
        expectThat(this).contentEquals(this.copyOf())
      }

      test("contents are not equal to a different array") {
        val expected = randomBytes()
        val error = fails {
          expectThat(this).contentEquals(expected)
        }
        assertEquals(
          "▼ Expect that 0x${toHex()}:\n" +
            "  ✗ array content equals 0x${expected.toHex()}",
          error.message
        )
      }

      test("contents are not equal to a sub-array") {
        val expected = copyOf(size / 2)
        val error = fails {
          expectThat(this).contentEquals(expected)
        }
        assertEquals(
          "▼ Expect that 0x${toHex()}:\n" +
            "  ✗ array content equals 0x${expected.toHex()}",
          error.message
        )
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
