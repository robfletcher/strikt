package strikt.assertions

import dev.minutest.TestDescriptor
import java.io.File
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission
import java.nio.file.attribute.PosixFilePermissions
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import strikt.api.expectThat

// TODO: improve how fixture Path's are generated since we leveraging @TempDir, which only gets created once for the entire minutest test context
internal object PathAssertions {

  private fun TestDescriptor.joinFullName() =
    fullName().joinToString(separator = "_")

  @TestFactory
  internal fun endsWith() = assertionTests<Path> {
    fixture { expectThat(Paths.get("startsWith", "endsWith")) }

    context("passes when the subject ends with") {
      test("the String type path") {
        endsWith("endsWith")
      }

      test("the Path type path") {
        endsWith(Paths.get("endsWith"))
      }
    }

    context("fails when the subject does not end with") {
      test("the String type path") {
        assertThrows<AssertionError> {
          endsWith("doesNotEndsWith")
        }
      }

      test("the Path type path") {
        assertThrows<AssertionError> {
          endsWith(Paths.get("doesNotEndsWith"))
        }
      }
    }
  }

  @TestFactory
  internal fun fileName() = assertionTests<Path> {
    fixture { expectThat(Paths.get("some", "path", "with", "name")) }

    test("maps to a Path of the file name") {
      fileName
        .isEqualTo(Paths.get("name"))
    }
  }

  @TestFactory
  internal fun isAbsolute() = assertionTests<Path> {
    context("when subject is an absolute path") {
      fixture { expectThat(Paths.get("/tmp", "path").toAbsolutePath()) }

      test("then assertion passes") {
        isAbsolute()
      }
    }

    context("when subject is not an absolute path") {
      fixture { expectThat(Paths.get("relative", "path")) }

      test("then assertion fails") {
        assertThrows<AssertionError> {
          isAbsolute()
        }
      }
    }
  }

  @TestFactory
  internal fun parent() = assertionTests<Path> {
    context("when subject is the root Path") {
      fixture { expectThat(Paths.get("/").root) }
      test("then the mapped value is null") {
        parent.isNull()
      }
    }

    context("when subject has a parent") {
      fixture { expectThat(Paths.get("parent", "child")) }
      test("then the mapped value is that parent") {
        parent
          .isNotNull()
          .isEqualTo(Paths.get("parent"))
      }
    }
  }

  @TestFactory
  internal fun resolve() = assertionTests<Path> {
    fixture { expectThat(Paths.get("parent")) }

    context("when the resolve value type is a Path") {
      test("then the mapped assertion is the resolved path") {
        resolve(Paths.get("child"))
          .isEqualTo(Paths.get("parent", "child"))
      }
    }

    context("when the resolve value type is a String") {
      test("then the mapped assertion is the resolved path") {
        resolve("child")
          .isEqualTo(Paths.get("parent", "child"))
      }
    }
  }

  @TestFactory
  internal fun startsWith() = assertionTests<Path> {
    fixture { expectThat(Paths.get("startsWith", "endsWith")) }

    context("passes when the subject starts with") {
      test("the String type path") {
        startsWith("startsWith")
      }

      test("the Path type path") {
        startsWith(Paths.get("startsWith"))
      }
    }

    context("fails when the subject does not start with") {
      test("the String type path") {
        assertThrows<AssertionError> {
          startsWith("doesNotStartWith")
        }
      }

      test("the Path type path") {
        assertThrows<AssertionError> {
          startsWith(Paths.get("doesNotStartWith"))
        }
      }
    }
  }

  @TestFactory
  internal fun toFile() = assertionTests<Path> {
    fixture { expectThat(Paths.get("path", "of", "file")) }
    test("mapped value is a File") {
      toFile()
        .isA<File>()
    }
  }

  @TestFactory
  internal fun exists(@TempDir directory: Path) = assertionTests<Path> {
    context("assertion passes") {
      context("when Path points to an existing file") {
        fixture { expectThat(Files.createFile(directory.resolve(it.name))) }
      }

      context("when Path points to a symlink that resolves to an existing file") {
        fixture {
          expectThat(
            Files.createSymbolicLink(
              directory.resolve("${it.joinFullName()}_symlink"),
              Files.createFile(directory.resolve("${it.joinFullName()}_target"))
            )
          )
        }

        test("and no link options are provided") {
          exists()
        }

        test("and the ${LinkOption.NOFOLLOW_LINKS} is provided") {
          exists(LinkOption.NOFOLLOW_LINKS)
        }
      }

      context("when Path points to a symlink that resolves to a nonexistent file") {
        fixture {
          val symlink = Files.createSymbolicLink(
            directory.resolve("${it.joinFullName()}_symlink"),
            directory.resolve("${it.joinFullName()}_nonexistent-target")
          )
          expectThat(symlink)
        }

        test("and the ${LinkOption.NOFOLLOW_LINKS} is provided") {
          exists(LinkOption.NOFOLLOW_LINKS)
        }
      }
    }

    context("assertion fails") {
      context("when Path points to a nonexistent file") {
        fixture { expectThat(directory.resolve(it.joinFullName())) }
      }
      context("when Path points to a symlink that resolves to an existing file") {
        fixture {
          val symlink = Files.createSymbolicLink(
            directory.resolve("${it.joinFullName()}_symlink"),
            Files.createFile(directory.resolve("${it.joinFullName()}_target"))
          )
          expectThat(symlink)
        }

        test("and no link options are provided") {
          exists()
        }

        test("and the ${LinkOption.NOFOLLOW_LINKS} is provided") {
          exists(LinkOption.NOFOLLOW_LINKS)
        }
      }

      context("when Path points to a symlink that resolves to a nonexistent file") {
        fixture {
          val symlink = Files.createSymbolicLink(
            directory.resolve("${it.joinFullName()}_symlink"),
            directory.resolve("${it.joinFullName()}_nonexistent-target")
          )
          expectThat(symlink)
        }

        test("and no link options are provided") {
          assertThrows<AssertionError> {
            exists()
          }
        }
      }
    }
  }

  @TestFactory
  internal fun isDirectory(@TempDir directory: Path) = assertionTests<Path> {
    context("when subject Path is a regular file") {
      fixture { expectThat(Files.createFile(directory.resolve(it.joinFullName()))) }
      test("assertion fails") {
        assertThrows<AssertionError> {
          isDirectory()
        }
      }
    }

    context("when subject Path is a directory") {
      fixture { expectThat(Files.createDirectory(directory.resolve(it.joinFullName()))) }
      test("assertion passes") {
        isDirectory()
      }
    }

    context("when subject Path is a symlink") {
      context("to a file") {
        fixture {
          expectThat(
            Files.createSymbolicLink(
              directory.resolve("${it.joinFullName()}_link"),
              Files.createFile(directory.resolve("${it.joinFullName()}_target"))
            )
          )
        }
        test("assertion fails") {
          assertThrows<AssertionError> {
            isDirectory()
          }
        }
      }

      context("to a directory") {
        fixture {
          expectThat(
            Files.createSymbolicLink(
              directory.resolve("${it.joinFullName()}_link"),
              Files.createDirectory(directory.resolve(it.joinFullName()))
            )
          )
        }
        test("succeeds when no link options are provided") {
          isDirectory()
        }
        test("fails when ${LinkOption.NOFOLLOW_LINKS} options is provided") {
          assertThrows<AssertionError> {
            isDirectory(LinkOption.NOFOLLOW_LINKS)
          }
        }
      }
    }
  }

  @TestFactory
  internal fun isExecutable(@TempDir directory: Path) = assertionTests<Path> {
    context("passes when") {
      fixture {
        expectThat(
          Files.createFile(
            directory.resolve(it.joinFullName()),
            PosixFilePermissions.asFileAttribute(
              setOf(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_EXECUTE
              )
            )
          )
        )
      }
      test("path has executable permission") {
        isExecutable()
      }
    }

    context("fails when") {
      fixture {
        expectThat(
          Files.createFile(
            directory.resolve(it.joinFullName()),
            PosixFilePermissions.asFileAttribute(setOf(PosixFilePermission.OWNER_READ))
          )
        )
      }
      test("path does not have executable permission") {
        assertThrows<AssertionError> {
          isExecutable()
        }
      }
    }
  }

  @TestFactory
  internal fun isReadable(@TempDir directory: Path) = assertionTests<Path> {
    fixture { expectThat(directory) }
    context("when subject is an existing directory") {
      test("then assertion passes") {
        isReadable()
      }
    }
  }

  @TestFactory
  internal fun isRegularFile(@TempDir directory: Path) = assertionTests<Path> {
    context("when subject is a regular file") {
      fixture { expectThat(Files.createFile(directory.resolve(it.joinFullName()))) }
      test("then assertion passes when no link options are provided") {
        isRegularFile()
      }

      test("then assertion passes when ${LinkOption.NOFOLLOW_LINKS} option is provide") {
        isRegularFile(LinkOption.NOFOLLOW_LINKS)
      }
    }

    context("subject is a directory") {
      fixture { expectThat(Files.createDirectory(directory.resolve(it.joinFullName()))) }
      test("then assertion fails") {
        assertThrows<AssertionError> {
          isRegularFile()
        }
      }
    }
  }

  @TestFactory
  internal fun isSymbolicLink(@TempDir directory: Path) = assertionTests<Path> {
    context("subject does not exist") {
      fixture { expectThat(directory.resolve(it.joinFullName())) }
      test("then assertion fails") {
        assertThrows<AssertionError> {
          isSymbolicLink()
        }
      }
    }

    context("subject is a regular file") {
      fixture { expectThat(Files.createFile(directory.resolve(it.joinFullName()))) }
      test("then assertion fails") {
        assertThrows<AssertionError> {
          isSymbolicLink()
        }
      }
    }

    context("subject is a symlink that resolves to an existing file") {
      context("that resolves to an existing file") {
        fixture {
          expectThat(
            Files.createSymbolicLink(
              directory.resolve("${it.joinFullName()}_symlink"),
              Files.createFile(directory.resolve("${it.joinFullName()}_target"))
            )
          )
        }

        test("assertion passes") {
          isSymbolicLink()
        }
      }
      context("that resolves to a nonexistent file") {
        fixture {
          expectThat(
            Files.createSymbolicLink(
              directory.resolve("${it.joinFullName()}_symlink"),
              directory.resolve("${it.joinFullName()}_target")
            )
          )
        }

        test("assertion passes") {
          isSymbolicLink()
        }
      }
    }
  }

  @TestFactory
  internal fun allBytes(@TempDir directory: Path) = assertionTests<Path> {
    context("subject is an empty file") {
      fixture {
        expectThat(Files.createFile(directory.resolve(it.joinFullName())))
      }
      test("then allBytes() maps to an empty byte array") {
        allBytes()
          .isEqualTo(byteArrayOf())
      }
    }

    context("subject is a single line file") {
      fixture {
        expectThat(
          Files.write(
            directory.resolve(it.joinFullName()),
            byteArrayOf(1, 2, 3, 4)
          )
        )
      }

      test("then allLines() maps to a singleton list of the line") {
        allBytes()
          .isEqualTo(byteArrayOf(1, 2, 3, 4))
      }
    }
  }

  @TestFactory
  internal fun allLines(@TempDir directory: Path) = assertionTests<Path> {
    context("subject is an empty file") {
      fixture {
        expectThat(Files.createFile(directory.resolve(it.joinFullName())))
      }
      test("then allLines() maps to an empty list") {
        allLines()
          .isEmpty()
      }
      test("then allLines(${Charsets.UTF_8}) maps to an empty list") {
        allLines(Charsets.UTF_8)
          .isEmpty()
      }
    }

    context("subject is a single line file") {
      fixture {
        expectThat(
          Files.write(
            directory.resolve(it.joinFullName()),
            listOf("first line")
          )
        )
      }

      test("then allLines() maps to a singleton list of the line") {
        allLines()
          .containsExactly("first line")
      }
      test("then allLines(${Charsets.UTF_8}) maps to a singleton list of the line") {
        allLines(Charsets.UTF_8)
          .containsExactly("first line")
      }
    }
  }

  @TestFactory
  internal fun size(@TempDir directory: Path) = assertionTests<Path> {

    context("subject is an empty file") {
      fixture {
        expectThat(
          Files.createFile(directory.resolve(it.joinFullName()))
        )
      }
      test("then size maps to a 0 ") {
        size
          .isEqualTo(0)
      }
    }

    context("subject is a 4-byte file") {
      fixture {
        expectThat(
          Files.write(
            Files.createFile(directory.resolve(it.joinFullName())),
            byteArrayOf(1, 2, 3, 4)
          )
        )
      }
      test("then size maps to a 4") {
        size
          .isEqualTo(4)
      }
    }
  }
}
