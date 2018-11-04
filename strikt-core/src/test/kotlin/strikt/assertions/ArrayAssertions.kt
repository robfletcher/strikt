package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
import strikt.internal.reporting.toHex
import java.lang.System.nanoTime
import java.util.Random

@DisplayName("assertions on array types")
internal object ArrayAssertions {

  @TestFactory
  fun `byte arrays`() = striktTests {
    val subject = randomBytes()

    subjectContext<ByteArray>("a byte array containing 0x${subject.toHex()}") {
      fixture { expectThat(subject) }

      context("contentEquals assertion") {
        test("contents are equal to a copy of itself") {
          contentEquals(subject.copyOf())
        }

        test("contents are not equal to a different array") {
          val expected = randomBytes()
          val error = assertThrows<AssertionError> {
            contentEquals(expected)
          }
          assertEquals(
            """▼ Expect that 0x${subject.toHex()}:
                |  ✗ array content equals 0x${expected.toHex()}"""
              .trimMargin(),
            error.message
          )
        }

        test("contents are not equal to a sub-array") {
          val expected = subject.copyOf(subject.size / 2)
          val error = assertThrows<AssertionError> {
            contentEquals(expected)
          }
          assertEquals(
            """▼ Expect that 0x${subject.toHex()}:
                |  ✗ array content equals 0x${expected.toHex()}"""
              .trimMargin(),
            error.message
          )
        }
      }

      context("isEqualTo assertion") {
        test("passes for a copy of itself") {
          isEqualTo(subject.copyOf())
        }

        test("fails for a different array") {
          val expected = randomBytes()
          val error = assertThrows<AssertionFailedError> {
            isEqualTo(expected)
          }
          assertEquals(
            """▼ Expect that 0x${subject.toHex()}:
                |  ✗ is equal to 0x${expected.toHex()} : found 0x${subject.toHex()}"""
              .trimMargin(),
            error.message
          )
        }

        test("fails if the expected value is null") {
          assertThrows<AssertionFailedError> {
            isEqualTo(null)
          }
        }
      }
    }
  }

  @TestFactory
  fun `character arrays`() = striktTests {
    val subject = "fnord".toCharArray()

    subjectContext<CharArray>("a char array containing ${subject.toList().map { "'$it'" }}") {
      fixture { expectThat(subject) }

      context("contentEquals assertion") {
        test("contents are equal to a copy of itself") {
          contentEquals(subject.copyOf())
        }

        test("contents are not equal to a different array") {
          val expected = "discord".toCharArray()
          val error = assertThrows<AssertionError> {
            contentEquals(expected)
          }
          assertEquals(
            """▼ Expect that ${subject.toList().map { "'$it'" }}:
                |  ✗ array content equals ${expected.toList().map { "'$it'" }}"""
              .trimMargin(),
            error.message
          )
        }

        test("contents are not equal to a sub-array") {
          val expected = subject.copyOf(subject.size / 2)
          val error = assertThrows<AssertionError> {
            contentEquals(expected)
          }
          assertEquals(
            """▼ Expect that ${subject.toList().map { "'$it'" }}:
              |  ✗ array content equals ${expected.toList().map { "'$it'" }}"""
              .trimMargin(),
            error.message
          )
        }
      }

      context("isEqualTo assertion") {
        test("passes for a copy of itself") {
          isEqualTo(subject.copyOf())
        }

        test("fails for a different array") {
          val expected = "discord".toCharArray()
          val error = assertThrows<AssertionFailedError> {
            isEqualTo(expected)
          }
          assertEquals(
            """▼ Expect that ${subject.toList().map { "'$it'" }}:
                |  ✗ is equal to ${expected.toList().map { "'$it'" }} : found ${subject.toList().map { "'$it'" }}"""
              .trimMargin(),
            error.message
          )
        }

        test("fails if the expected value is null") {
          assertThrows<AssertionFailedError> {
            isEqualTo(null)
          }
        }
      }
    }
  }

  @TestFactory
  fun `boolean arrays`() = striktTests {
    val subject = BooleanArray(8) { it % 2 == 0 }

    subjectContext<BooleanArray>("isEqualTo assertion") {
      fixture { expectThat(subject) }

      test("passes for a copy of itself") {
        isEqualTo(subject.copyOf())
      }
    }
  }

  @TestFactory
  fun `short arrays`() = striktTests {
    val subject = shortArrayOf(1, 2, 4, 8, 16, 32, 64, 128)

    subjectContext<ShortArray>("isEqualTo assertion") {
      fixture { expectThat(subject) }

      test("passes for a copy of itself") {
        isEqualTo(subject.copyOf())
      }
    }
  }

  @TestFactory
  fun `int arrays`() = striktTests {
    val subject = intArrayOf(1, 2, 4, 8, 16, 32, 64, 128)

    subjectContext<IntArray>("isEqualTo assertion") {
      fixture { expectThat(subject) }

      test("passes for a copy of itself") {
        isEqualTo(subject.copyOf())
      }
    }
  }

  @TestFactory
  fun `long arrays`() = striktTests {
    val subject = longArrayOf(1, 2, 4, 8, 16, 32, 64, 128)

    subjectContext<LongArray>("isEqualTo assertion") {
      fixture { expectThat(subject) }

      test("passes for a copy of itself") {
        isEqualTo(subject.copyOf())
      }
    }
  }

  @TestFactory
  fun `float arrays`() = striktTests {
    val subject = floatArrayOf(4.2f, 128.3f, 64.5f, 32.9f)

    subjectContext<FloatArray>("isEqualTo assertion") {
      fixture { expectThat(subject) }

      test("passes for a copy of itself") {
        isEqualTo(subject.copyOf())
      }
    }
  }

  @TestFactory
  fun `double arrays`() = striktTests {
    val subject = doubleArrayOf(4.2, 128.3, 64.5, 32.9)

    subjectContext<DoubleArray>("isEqualTo assertion") {
      fixture { expectThat(subject) }

      test("passes for a copy of itself") {
        isEqualTo(subject.copyOf())
      }
    }
  }

  @TestFactory
  fun `object arrays`() = striktTests {
    val subject = arrayOf("catflap", "rubberplant", "marzipan")

    subjectContext<Array<String>>("isEqualTo assertion") {
      fixture { expectThat(subject) }

      test("passes for a copy of itself") {
        isEqualTo(subject.copyOf())
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
