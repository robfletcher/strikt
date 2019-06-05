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
  fun `byte arrays`() = assertionTests<ByteArray> {
    val subject = randomBytes()

    context("a byte array containing 0x${subject.toHex()}") {
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

      context("isEmpty assertion") {
        test("fails") {
          assertThrows<AssertionFailedError> {
            isEmpty()
          }
        }
      }
    }

    context("an empty byte array") {
      fixture { expectThat(byteArrayOf()) }

      context("isEmpty assertion") {
        test("passes") {
          isEmpty()
        }
      }
    }
  }

  @TestFactory
  fun `character arrays`() = assertionTests<CharArray> {
    val subject = "fnord".toCharArray()

    context("a char array containing ${subject.toList().map { "'$it'" }}") {
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

      context("isEmpty assertion") {
        test("fails") {
          assertThrows<AssertionFailedError> {
            isEmpty()
          }
        }
      }
    }

    context("an empty character array") {
      fixture { expectThat(charArrayOf()) }

      context("isEmpty assertion") {
        test("passes") {
          isEmpty()
        }
      }
    }
  }

  @TestFactory
  fun `boolean arrays`() = assertionTests<BooleanArray> {
    val subject = BooleanArray(8) { it % 2 == 0 }

    context("a boolean array containing ${subject.contentToString()}") {
      fixture { expectThat(subject) }

      context("isEqualTo assertion") {
        test("passes for a copy of itself") {
          isEqualTo(subject.copyOf())
        }
      }

      context("isEmpty assertion") {
        test("fails") {
          assertThrows<AssertionFailedError> {
            isEmpty()
          }
        }
      }
    }

    context("an empty boolean array") {
      fixture { expectThat(booleanArrayOf()) }

      context("isEmpty assertion") {
        test("passes") {
          isEmpty()
        }
      }
    }
  }

  @TestFactory
  fun `short arrays`() = assertionTests<ShortArray> {
    val subject = shortArrayOf(1, 2, 4, 8, 16, 32, 64, 128)

    context("a short array containing ${subject.contentToString()}") {
      fixture { expectThat(subject) }

      context("isEqualTo assertion") {
        test("passes for a copy of itself") {
          isEqualTo(subject.copyOf())
        }
      }

      context("isEmpty assertion") {
        test("fails") {
          assertThrows<AssertionFailedError> {
            isEmpty()
          }
        }
      }
    }

    context("an empty short array") {
      fixture { expectThat(shortArrayOf()) }

      context("isEmpty assertion") {
        test("passes") {
          isEmpty()
        }
      }
    }
  }

  @TestFactory
  fun `int arrays`() = assertionTests<IntArray> {
    val subject = intArrayOf(1, 2, 4, 8, 16, 32, 64, 128)

    context("an int array containing ${subject.contentToString()}") {
      fixture { expectThat(subject) }

      context("isEqualTo assertion") {
        test("passes for a copy of itself") {
          isEqualTo(subject.copyOf())
        }
      }

      context("isEmpty assertion") {
        test("fails") {
          assertThrows<AssertionFailedError> {
            isEmpty()
          }
        }
      }
    }

    context("an empty int array") {
      fixture { expectThat(intArrayOf()) }
      context("isEmpty assertion") {
        test("passes") {
          isEmpty()
        }
      }
    }
  }

  @TestFactory
  fun `long arrays`() = assertionTests<LongArray> {
    val subject = longArrayOf(1, 2, 4, 8, 16, 32, 64, 128)

    context("a long array containing ${subject.contentToString()}") {
      fixture { expectThat(subject) }

      context("isEqualTo assertion") {

        test("passes for a copy of itself") {
          isEqualTo(subject.copyOf())
        }
      }

      context("isEmpty assertion") {
        test("fails") {
          assertThrows<AssertionFailedError> {
            isEmpty()
          }
        }
      }
    }

    context("an empty long array") {
      fixture { expectThat(longArrayOf()) }
      context("isEmpty assertion") {
        test("passes") {
          isEmpty()
        }
      }
    }
  }

  @TestFactory
  fun `float arrays`() = assertionTests<FloatArray> {
    val subject = floatArrayOf(4.2f, 128.3f, 64.5f, 32.9f)

    context("a float array containing ${subject.contentToString()}") {
      fixture { expectThat(subject) }

      context("isEqualTo assertion") {

        test("passes for a copy of itself") {
          isEqualTo(subject.copyOf())
        }
      }

      context("isEmpty assertion") {
        test("fails") {
          assertThrows<AssertionFailedError> {
            isEmpty()
          }
        }
      }
    }

    context("an empty float array") {
      fixture { expectThat(floatArrayOf()) }
      context("isEmpty assertion") {
        test("passes") {
          isEmpty()
        }
      }
    }
  }

  @TestFactory
  fun `double arrays`() = assertionTests<DoubleArray> {
    val subject = doubleArrayOf(4.2, 128.3, 64.5, 32.9)

    context("a double array containing ${subject.contentToString()}") {
      fixture { expectThat(subject) }

      context("isEqualTo assertion") {

        test("passes for a copy of itself") {
          isEqualTo(subject.copyOf())
        }
      }

      context("isEmpty assertion") {
        test("fails") {
          assertThrows<AssertionFailedError> {
            isEmpty()
          }
        }
      }
    }

    context("an empty double array") {
      fixture { expectThat(doubleArrayOf()) }
      context("isEmpty assertion") {
        test("passes") {
          isEmpty()
        }
      }
    }
  }

  @TestFactory
  fun `object arrays`() = assertionTests<Array<String>> {
    val subject = arrayOf("catflap", "rubberplant", "marzipan")

    context("an object array containing ${subject.contentToString()}") {
      fixture { expectThat(subject) }

      context("isEqualTo assertion") {

        test("passes for a copy of itself") {
          isEqualTo(subject.copyOf())
        }
      }

      context("isEmpty assertion") {
        test("fails") {
          assertThrows<AssertionFailedError> {
            isEmpty()
          }
        }
      }
    }

    context("an empty object array") {
      fixture { expectThat(arrayOf()) }
      context("isEmpty assertion") {
        test("passes") {
          isEmpty()
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
