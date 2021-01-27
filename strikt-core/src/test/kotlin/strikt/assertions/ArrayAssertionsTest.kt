package strikt.assertions

import failfast.describe
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
import strikt.internal.reporting.toHex
import java.lang.System.nanoTime
import java.util.Random

internal object ArrayAssertionsTest {

  val context = describe("assertions on array types") {
    context("byte arrays") {
      val subject = randomBytes()

      context("a byte array containing 0x${subject.toHex()}") {
        val assertion = expectThat(subject)

        context("contentEquals assertion") {
          test("contents are equal to a copy of itself") {
            assertion.contentEquals(subject.copyOf())
          }

          test("contents are not equal to a different array") {
            val expected = randomBytes()
            val error = assertThrows<AssertionError> {
              assertion.contentEquals(expected)
            }
            expectThat(error.message).isEqualTo(
              """▼ Expect that 0x${subject.toHex()}:
                |  ✗ array content equals 0x${expected.toHex()}"""
                .trimMargin()
            )
          }

          test("contents are not equal to a sub-array") {
            val expected = subject.copyOf(subject.size / 2)
            val error = assertThrows<AssertionError> {
              assertion.contentEquals(expected)
            }
            expectThat(error.message).isEqualTo(
              """▼ Expect that 0x${subject.toHex()}:
                |  ✗ array content equals 0x${expected.toHex()}"""
                .trimMargin()
            )
          }
        }

        context("isEqualTo assertion") {
          test("passes for a copy of itself") {
            assertion.isEqualTo(subject.copyOf())
          }

          test("fails for a different array") {
            val expected = randomBytes()
            val error = assertThrows<AssertionFailedError> {
              assertion.isEqualTo(expected)
            }
            expectThat(error.message).isEqualTo(
              """▼ Expect that 0x${subject.toHex()}:
                |  ✗ is equal to 0x${expected.toHex()}
                |          found 0x${subject.toHex()}"""
                .trimMargin()
            )
          }

          test("fails if the expected value is null") {
            assertThrows<AssertionFailedError> {
              assertion.isEqualTo(null)
            }
          }
        }

        context("isEmpty assertion") {
          test("fails") {
            assertThrows<AssertionFailedError> {
              assertion.isEmpty()
            }
          }
        }
      }

      context("an empty byte array") {
        val assertion = expectThat(byteArrayOf())

        context("isEmpty assertion") {
          test("passes") {
            assertion.isEmpty()
          }
        }
      }
    }

    context("character arrays") {
      val subject = "fnord".toCharArray()

      context("a char array containing ${subject.toList().map { "'$it'" }}") {
        val assertion = expectThat(subject)

        context("contentEquals assertion") {
          test("contents are equal to a copy of itself") {
            assertion.contentEquals(subject.copyOf())
          }

          test("contents are not equal to a different array") {
            val expected = "discord".toCharArray()
            val error = assertThrows<AssertionError> {
              assertion.contentEquals(expected)
            }
            expectThat(error.message).isEqualTo(
              """▼ Expect that ${subject.toList().map { "'$it'" }}:
                |  ✗ array content equals ${expected.toList().map { "'$it'" }}"""
                .trimMargin()
            )
          }

          test("contents are not equal to a sub-array") {
            val expected = subject.copyOf(subject.size / 2)
            val error = assertThrows<AssertionError> {
              assertion.contentEquals(expected)
            }
            expectThat(error.message).isEqualTo(
              """▼ Expect that ${subject.toList().map { "'$it'" }}:
              |  ✗ array content equals ${expected.toList().map { "'$it'" }}"""
                .trimMargin()
            )
          }
        }

        context("isEqualTo assertion") {
          test("passes for a copy of itself") {
            assertion.isEqualTo(subject.copyOf())
          }

          test("fails for a different array") {
            val expected = "discord".toCharArray()
            val error = assertThrows<AssertionFailedError> {
              assertion.isEqualTo(expected)
            }
            expectThat(error.message).isEqualTo(
              """▼ Expect that ${subject.toList().map { "'$it'" }}:
                |  ✗ is equal to ${expected.toList().map { "'$it'" }}
                |          found ${subject.toList().map { "'$it'" }}"""
                .trimMargin()
            )
          }

          test("fails if the expected value is null") {
            assertThrows<AssertionFailedError> {
              assertion.isEqualTo(null)
            }
          }
        }

        context("isEmpty assertion") {
          test("fails") {
            assertThrows<AssertionFailedError> {
              assertion.isEmpty()
            }
          }
        }
      }

      context("an empty character array") {
        val assertion = expectThat(charArrayOf())

        context("isEmpty assertion") {
          test("passes") {
            assertion.isEmpty()
          }
        }
      }
    }

    context("boolean arrays") {
      val subject = BooleanArray(8) { it % 2 == 0 }

      context("a boolean array containing ${subject.contentToString()}") {
        val assertion = expectThat(subject)

        context("isEqualTo assertion") {
          test("passes for a copy of itself") {
            assertion.isEqualTo(subject.copyOf())
          }
        }

        context("isEmpty assertion") {
          test("fails") {
            assertThrows<AssertionFailedError> {
              assertion.isEmpty()
            }
          }
        }
      }

      context("an empty boolean array") {
        val assertion = expectThat(booleanArrayOf())

        context("isEmpty assertion") {
          test("passes") {
            assertion.isEmpty()
          }
        }
      }
    }

    context("short arrays") {
      val subject = shortArrayOf(1, 2, 4, 8, 16, 32, 64, 128)

      context("a short array containing ${subject.contentToString()}") {
        val assertion = expectThat(subject)

        context("isEqualTo assertion") {
          test("passes for a copy of itself") {
            assertion.isEqualTo(subject.copyOf())
          }
        }

        context("isEmpty assertion") {
          test("fails") {
            assertThrows<AssertionFailedError> {
              assertion.isEmpty()
            }
          }
        }
      }

      context("an empty short array") {
        val assertion = expectThat(shortArrayOf())

        context("isEmpty assertion") {
          test("passes") {
            assertion.isEmpty()
          }
        }
      }
    }

    context("int arrays") {
      val subject = intArrayOf(1, 2, 4, 8, 16, 32, 64, 128)

      context("an int array containing ${subject.contentToString()}") {
        val assertion = expectThat(subject)

        context("isEqualTo assertion") {
          test("passes for a copy of itself") {
            assertion.isEqualTo(subject.copyOf())
          }
        }

        context("isEmpty assertion") {
          test("fails") {
            assertThrows<AssertionFailedError> {
              assertion.isEmpty()
            }
          }
        }
      }

      context("an empty int array") {
        val assertion = expectThat(intArrayOf())
        context("isEmpty assertion") {
          test("passes") {
            assertion.isEmpty()
          }
        }
      }
    }

    context("long arrays") {
      val subject = longArrayOf(1, 2, 4, 8, 16, 32, 64, 128)

      context("a long array containing ${subject.contentToString()}") {
        val assertion = expectThat(subject)

        context("isEqualTo assertion") {

          test("passes for a copy of itself") {
            assertion.isEqualTo(subject.copyOf())
          }
        }

        context("isEmpty assertion") {
          test("fails") {
            assertThrows<AssertionFailedError> {
              assertion.isEmpty()
            }
          }
        }
      }

      context("an empty long array") {
        val assertion = expectThat(longArrayOf())
        context("isEmpty assertion") {
          test("passes") {
            assertion.isEmpty()
          }
        }
      }
    }

    context("float arrays") {
      val subject = floatArrayOf(4.2f, 128.3f, 64.5f, 32.9f)

      context("a float array containing ${subject.contentToString()}") {
        val assertion = expectThat(subject)

        context("isEqualTo assertion") {

          test("passes for a copy of itself") {
            assertion.isEqualTo(subject.copyOf())
          }
        }

        context("isEmpty assertion") {
          test("fails") {
            assertThrows<AssertionFailedError> {
              assertion.isEmpty()
            }
          }
        }
      }

      context("an empty float array") {
        val assertion = expectThat(floatArrayOf())
        context("isEmpty assertion") {
          test("passes") {
            assertion.isEmpty()
          }
        }
      }
    }

    context("double arrays") {
      val subject = doubleArrayOf(4.2, 128.3, 64.5, 32.9)

      context("a double array containing ${subject.contentToString()}") {
        val assertion = expectThat(subject)

        context("isEqualTo assertion") {

          test("passes for a copy of itself") {
            assertion.isEqualTo(subject.copyOf())
          }
        }

        context("isEmpty assertion") {
          test("fails") {
            assertThrows<AssertionFailedError> {
              assertion.isEmpty()
            }
          }
        }
      }

      context("an empty double array") {
        val assertion = expectThat(doubleArrayOf())
        context("isEmpty assertion") {
          test("passes") {
            assertion.isEmpty()
          }
        }
      }
    }

    context("object arrays") {
      val subject = arrayOf("catflap", "rubberplant", "marzipan")

      context("an object array containing ${subject.contentToString()}") {
        val assertion = expectThat(subject)

        context("isEqualTo assertion") {

          test("passes for a copy of itself") {
            assertion.isEqualTo(subject.copyOf())
          }
        }

        context("isEmpty assertion") {
          test("fails") {
            assertThrows<AssertionFailedError> {
              assertion.isEmpty()
            }
          }
        }
      }

      context("an empty object array") {
        val assertion = expectThat(arrayOf<Any>())
        context("isEmpty assertion") {
          test("passes") {
            assertion.isEmpty()
          }
        }
      }
    }

    test("can map an array to a list for easier matching") {
      val array = arrayOf("catflap", "rubberplant", "marzipan")
      expectThat(array).toList().first().isEqualTo("catflap")
    }
  }
}

internal fun randomBytes(): ByteArray =
  ByteArray(8).also(Random(nanoTime())::nextBytes)
