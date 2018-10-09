package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.fails
import strikt.internal.reporting.toHex
import java.lang.System.nanoTime
import java.util.Random

@DisplayName("assertions on array types")
internal class ArrayAssertions {
  @Nested
  @DisplayName("contentEquals")
  inner class ContentEquals {
    @Test
    fun `byte arrays are equal if their contents are the same`() {
      val array = randomBytes()
      expectThat(array).contentEquals(array.copyOf())
    }

    @TestFactory
    fun `a byte array is not equal to a different array`() =
      listOf(
        Pair(randomBytes(), randomBytes()),
        randomBytes().let { Pair(it, it.copyOf(it.size / 2)) }
      ).map { (array, other) ->
        dynamicTest("0x${array.toHex()} does not equal 0x${other.toHex()}") {
          val error = fails {
            expectThat(array).contentEquals(other)
          }
          assertEquals(
            "▼ Expect that 0x${array.toHex()}:\n" +
              "  ✗ array content equals 0x${other.toHex()}",
            error.message
          )
        }
      }

    @Test
    fun `char arrays are equal if their contents are the same`() {
      val array = randomBytes()
      expectThat(array).contentEquals(array.copyOf())
    }

    @TestFactory
    fun `a char array is not equal to a different array`() =
      listOf(
        Pair(randomBytes(), randomBytes()),
        randomBytes().let { Pair(it, it.copyOf(it.size / 2)) }
      ).map { (array, other) ->
        dynamicTest("0x${array.toHex()} does not equal 0x${other.toHex()}") {
          val error = fails {
            expectThat(array).contentEquals(other)
          }
          assertEquals(
            "▼ Expect that 0x${array.toHex()}:\n" +
              "  ✗ array content equals 0x${other.toHex()}",
            error.message
          )
        }
      }
  }
}

internal fun randomBytes(): ByteArray =
  ByteArray(8).also(Random(nanoTime())::nextBytes)
