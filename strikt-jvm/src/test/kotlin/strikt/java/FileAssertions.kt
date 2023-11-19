package strikt.java

import dev.minutest.TestDescriptor
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.endsWith
import strikt.assertions.isA
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.map
import strikt.assertions.startsWith
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

// TODO: improve how fixture Path's are generated since we leveraging @TempDir, which only gets created once for the entire minutest test context
@DisplayName("assertions on java.io.File")
internal object FileAssertions : JUnit5Minutests {
  @TempDir lateinit var directory: Path

  private fun TestDescriptor.joinFullName() = fullName().joinToString(separator = "_")

  fun tests() =
    rootContext<Assertion.Builder<File>> {
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

      context("parentFile") {
        context("subject parentFile exists") {
          fixture { expectThat(File("parent", "child")) }
          test("then parentFile maps to the parent file object") {
            parentFile
              .isEqualTo(File("parent"))
          }
        }

        context("subject file is root") {
          fixture { expectThat(File("/")) }
          test("then parentFile maps to a null value") {
            parentFile
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

      context("lastModified") {
        context("given the subject is a file with last modified") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setLastModified(1602491019000L)
              }
            )
          }
          test("then the mapped value is the last modified") {
            lastModified
              .isEqualTo(1602491019000L)
          }
        }
      }

      context("length") {
        context("given the subject is a file with length") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                writeBytes("Hello World".toByteArray(Charsets.US_ASCII)) // us-ascii enforce 1 byte per character
              }
            )
          }
          test("then the mapped value is the length") {
            length
              .isEqualTo(11L)
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

      context("childFiles") {
        context("given the subject is a regular file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile())
            )
          }
          test("then the mapped value is empty") {
            childFiles
              .isEmpty()
          }
        }

        context("given the subject is a directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                File(this, "foo.txt").apply {
                  createNewFile()
                }
                File(this, "bar.zip").apply {
                  createNewFile()
                }
                File(this, "zap.tar").apply {
                  createNewFile()
                }
              }
            )
          }
          test("then the mapped value contains all children") {
            childFiles
              .map { it.name }
              .containsExactlyInAnyOrder("foo.txt", "bar.zip", "zap.tar")
          }
        }
      }

      context("childFile") {
        context("given the subject is a regular file") {
          fixture { expectThat(File.createTempFile("example", ".txt", directory.toFile())) }
          test("then the mapped value is null") {
            childFile("foo.txt")
              .and {
                name.isEqualTo("foo.txt")
                parentFile
                  .isNotNull()
                  .name
                  .startsWith("example")
                  .endsWith(".txt")
              }
          }
        }

        context("given the subject is a directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                File(this, "foo.txt").apply {
                  createNewFile()
                }
                File(this, "bar.zip").apply {
                  createNewFile()
                }
                File(this, "zap.tar").apply {
                  createNewFile()
                }
              }
            )
          }
          test("then the mapped value contains the children") {
            childFile("foo.txt")
              .and {
                name.isEqualTo("foo.txt")
                parentFile
                  .isNotNull()
                  .name
                  .startsWith("example")
                assertThat("is an existing file") {
                  it.exists()
                }
              }
          }
        }
      }

      context("exists") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              exists()
            }
          }
        }

        context("given the subject is an existing file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile())
            )
          }
          test("then the assertion should pass") {
            exists()
          }
        }
      }

      context("notExists") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should pass") {
            notExists()
          }
        }

        context("given the subject is an existing file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile())
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              notExists()
            }
          }
        }
      }

      context("isRegularFile") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isRegularFile()
            }
          }
        }

        context("given the subject is an existing file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile())
            )
          }
          test("then the assertion should pass") {
            isRegularFile()
          }
        }

        context("given the subject is an existing directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile()
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isRegularFile()
            }
          }
        }
      }

      context("isNotRegularFile") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should pass") {
            isNotRegularFile()
          }
        }

        context("given the subject is an existing file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile())
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isNotRegularFile()
            }
          }
        }

        context("given the subject is an existing directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile()
            )
          }
          test("then the assertion should pass") {
            isNotRegularFile()
          }
        }
      }

      context("isDirectory") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isDirectory()
            }
          }
        }

        context("given the subject is an existing file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile())
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isDirectory()
            }
          }
        }

        context("given the subject is an existing directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile()
            )
          }
          test("then the assertion should pass") {
            isDirectory()
          }
        }
      }

      context("isNotDirectory") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should pass") {
            isNotDirectory()
          }
        }

        context("given the subject is an existing file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile())
            )
          }
          test("then the assertion should pass") {
            isNotDirectory()
          }
        }

        context("given the subject is an existing directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile()
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isNotDirectory()
            }
          }
        }
      }

      context("isReadable") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isReadable()
            }
          }
        }

        context("given the subject is an existing readable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setReadable(true, false)
              }
            )
          }
          test("then the assertion should pass") {
            isReadable()
          }
        }

        context("given the subject is an existing readable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setReadable(true, false)
              }
            )
          }
          test("then the assertion should pass") {
            isReadable()
          }
        }

        context("given the subject is an existing not readable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setReadable(false, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isReadable()
            }
          }
          after {
            get {
              setReadable(true, false)
            }
          }
        }

        context("given the subject is an existing not readable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setReadable(false, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isReadable()
            }
          }
          after {
            get {
              setReadable(true, false)
            }
          }
        }
      }

      context("isNotReadable") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should pass") {
            isNotReadable()
          }
        }

        context("given the subject is an existing readable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setReadable(true, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isNotReadable()
            }
          }
        }

        context("given the subject is an existing readable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setReadable(true, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isNotReadable()
            }
          }
        }

        context("given the subject is an existing not readable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setReadable(false, false)
              }
            )
          }
          test("then the assertion should pass") {
            isNotReadable()
          }
          after {
            get {
              setReadable(true, false)
            }
          }
        }

        context("given the subject is an existing not readable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setReadable(false, false)
              }
            )
          }
          test("then the assertion should pass") {
            isNotReadable()
          }
          after {
            get {
              setReadable(true, false)
            }
          }
        }
      }

      context("isWritable") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isWritable()
            }
          }
        }

        context("given the subject is an existing writable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setWritable(true, false)
              }
            )
          }
          test("then the assertion should pass") {
            isWritable()
          }
        }

        context("given the subject is an existing writable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setWritable(true, false)
              }
            )
          }
          test("then the assertion should pass") {
            isWritable()
          }
        }

        context("given the subject is an existing not writable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setWritable(false, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isWritable()
            }
          }
        }

        context("given the subject is an existing not writable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setWritable(false, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isWritable()
            }
          }
        }
      }

      context("isNotWritable") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should pass") {
            isNotWritable()
          }
        }

        context("given the subject is an existing writable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setWritable(true, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isNotWritable()
            }
          }
        }

        context("given the subject is an existing writable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setWritable(true, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isNotWritable()
            }
          }
        }

        context("given the subject is an existing not writable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setWritable(false, false)
              }
            )
          }
          test("then the assertion should pass") {
            isNotWritable()
          }
        }

        context("given the subject is an existing not writable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setWritable(false, false)
              }
            )
          }
          test("then the assertion should pass") {
            isNotWritable()
          }
        }
      }

      context("isExecutable") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isExecutable()
            }
          }
        }

        context("given the subject is an existing executable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setExecutable(true, false)
              }
            )
          }
          test("then the assertion should pass") {
            isExecutable()
          }
        }

        context("given the subject is an existing executable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setExecutable(true, false)
              }
            )
          }
          test("then the assertion should pass") {
            isExecutable()
          }
        }

        context("given the subject is an existing not executable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setExecutable(false, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isExecutable()
            }
          }
          after {
            get {
              setExecutable(true, false)
            }
          }
        }

        context("given the subject is an existing not executable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setExecutable(false, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isExecutable()
            }
          }
          after {
            get {
              setExecutable(true, false)
            }
          }
        }
      }

      context("isNotExecutable") {
        context("given the subject is a non-existing file") {
          fixture {
            expectThat(
              File("example.txt")
            )
          }
          test("then the assertion should pass") {
            isNotExecutable()
          }
        }

        context("given the subject is an existing executable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setExecutable(true, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isNotExecutable()
            }
          }
        }

        context("given the subject is an existing executable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setExecutable(true, false)
              }
            )
          }
          test("then the assertion should fail") {
            assertThrows<AssertionFailedError> {
              isNotExecutable()
            }
          }
        }

        context("given the subject is an existing not executable file") {
          fixture {
            expectThat(
              File.createTempFile("example", ".txt", directory.toFile()).apply {
                setExecutable(false, false)
              }
            )
          }
          test("then the assertion should pass") {
            isNotExecutable()
          }
          after {
            get {
              setExecutable(true, false)
            }
          }
        }

        context("given the subject is an existing not executable directory") {
          fixture {
            expectThat(
              Files.createTempDirectory(directory, "example").toFile().apply {
                setExecutable(false, false)
              }
            )
          }
          test("then the assertion should pass") {
            isNotExecutable()
          }
          after {
            get {
              setExecutable(true, false)
            }
          }
        }
      }
    }
}
