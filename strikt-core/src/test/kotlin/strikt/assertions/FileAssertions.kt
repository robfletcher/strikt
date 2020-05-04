package strikt.assertions

import dev.minutest.TestDescriptor
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.io.TempDir
import strikt.api.Assertion
import strikt.api.expectThat
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

// TODO: improve how fixture Path's are generated since we leveraging @TempDir, which only gets created once for the entire minutest test context
@DisplayName("assertions on java.io.File")
internal object FileAssertions : JUnit5Minutests {

  @TempDir lateinit var directory: Path

  private fun TestDescriptor.joinFullName() =
    fullName().joinToString(separator = "_")

  fun tests() = rootContext<Assertion.Builder<File>> {
    context("name") {
      context("subject parent exists") {
        fixture { expectThat(File("example.txt")) }
        test("then name maps to the file name") {
          name
            .isEqualTo("example.txt")
        }
      }

      context("subject file is root") {
        fixture { expectThat(File("/")) }
        test("then name maps to an empty string") {
          name
            .isEmpty()
        }
      }
    }

    context("parent") {
      context("subject parent exists") {
        fixture { expectThat(File("parent", "child")) }
        test("then parent maps to the parent file") {
          parent
            .isEqualTo("parent")
        }
      }

      context("subject file is root") {
        fixture { expectThat(File("/")) }
        test("then parent maps to a null value") {
          parent
            .isNull()
        }
      }
    }

    context("toPath") {
      fixture { expectThat(File("parent", "child")) }
      test("mapped value is a Path") {
        toPath()
          .isA<Path>()
      }
    }

    context("extension") {
      context("given the subject is a file with an extension") {
        fixture { expectThat(File("example.txt")) }
        test("then the mapped value is the extension") {
          extension
            .isEqualTo("txt")
        }
      }

      context("given the subject is a file without an extension") {
        fixture { expectThat(File("example")) }
        test("then the mapped value is an empty string") {
          extension
            .isEmpty()
        }
      }
    }

    context("nameWithoutExtension") {
      context("given the subject is a file with an extension") {
        fixture { expectThat(File("example.txt")) }
        test("then the mapped value is the extension") {
          nameWithoutExtension
            .isEqualTo("example")
        }
      }

      context("given the subject is a file without an extension") {
        fixture { expectThat(File("example")) }
        test("then the mapped value is the filename") {
          nameWithoutExtension
            .isEqualTo("example")
        }
      }
    }

    context("lines") {
      context("subject is an empty file") {
        fixture {
          expectThat(
            Files.createFile(directory.resolve(it.joinFullName())).toFile()
          )
        }
        test("then lines() maps to an empty list") {
          lines()
            .isEmpty()
        }
        test("then lines(${Charsets.UTF_8}) maps to an empty list") {
          lines(Charsets.UTF_8)
            .isEmpty()
        }
      }

      context("subject is a single line file") {
        fixture {
          expectThat(
            Files.write(
              directory.resolve(it.joinFullName()),
              listOf("first line")
            ).toFile()
          )
        }

        test("then lines() maps to a singleton list of the line") {
          lines()
            .containsExactly("first line")
        }
        test("then lines(${Charsets.UTF_8}) maps to a singleton list of the line") {
          lines(Charsets.UTF_8)
            .containsExactly("first line")
        }
      }
    }

    context("text") {
      context("subject is an empty file") {
        fixture {
          expectThat(
            Files.createFile(directory.resolve(it.joinFullName())).toFile()
          )
        }
        test("then text() maps to an empty list") {
          text()
            .isEmpty()
        }
        test("then text(${Charsets.UTF_8}) maps to an empty list") {
          text(Charsets.UTF_8)
            .isEmpty()
        }
      }

      context("subject is a single line file") {
        fixture {
          expectThat(
            Files.write(
              directory.resolve(it.joinFullName()),
              "first line".toByteArray()
            ).toFile()
          )
        }

        test("then text() maps to the file content") {
          text()
            .isEqualTo("first line")
        }
        test("then lines(${Charsets.UTF_8}) maps to the file content") {
          text(Charsets.UTF_8)
            .isEqualTo("first line")
        }
      }
    }
  }
}
