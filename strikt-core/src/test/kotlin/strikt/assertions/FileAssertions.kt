package strikt.assertions

import dev.minutest.TestDescriptor
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.io.TempDir
import strikt.api.expectThat
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

// TODO: improve how fixture Path's are generated since we leveraging @TempDir, which only gets created once for the entire minutest test context
@DisplayName("assertions on java.io.File")
internal object FileAssertions {

  private fun TestDescriptor.joinFullName() = fullName().joinToString(separator = "_")

  @TestFactory
  internal fun name() = assertionTests<File> {
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

  @TestFactory
  internal fun parent() = assertionTests<File> {
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

  @TestFactory
  internal fun toPath() = assertionTests<File> {
    fixture { expectThat(File("parent", "child")) }
    test("mapped value is a Path") {
      toPath()
        .isA<Path>()
    }
  }

  @TestFactory
  internal fun extension() = assertionTests<File> {
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

  @TestFactory
  internal fun nameWithoutExtension() = assertionTests<File> {
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

  @TestFactory
  internal fun lines(@TempDir directory: Path) = assertionTests<File> {
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
          Files.write(directory.resolve(it.joinFullName()), listOf("first line")).toFile()
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

  @TestFactory
  internal fun text(@TempDir directory: Path) = assertionTests<File> {
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
          Files.write(directory.resolve(it.joinFullName()), "first line".toByteArray()).toFile()
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
