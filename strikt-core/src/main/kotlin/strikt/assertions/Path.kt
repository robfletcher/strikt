package strikt.assertions

import strikt.api.Assertion.Builder
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path

// java.nio.file.Path
/**
 * Asserts that the subject end with the provided path.
 *
 * @param other the given path
 * @see Path.endsWith
 */
infix fun <T : Path> Builder<T>.endsWith(other: Path): Builder<T> =
  assert("ends with %s", other) {
    if (it.endsWith(other)) {
      pass()
    } else {
      fail(actual = it)
    }
  }

/**
 * Asserts that the subject end with the provided path string.
 *
 * @param other the given path string
 * @see Path.endsWith
 */
infix fun <T : Path> Builder<T>.endsWith(other: String): Builder<T> =
  assert("ends with %s", other) {
    if (it.endsWith(other)) {
      pass()
    } else {
      fail(actual = it)
    }
  }

/**
 * Maps this assertion to an assertion on the path representing the name of the subject.
 *
 * @see Path.getFileName
 */
val <T : Path> Builder<T>.fileName: Builder<Path>
  get() = get("file name", Path::getFileName)

/**
 * Asserts that the subject is an absolute path.
 *
 * @see Path.isAbsolute
 */
fun <T : Path> Builder<T>.isAbsolute(): Builder<T> =
  assertThat("is absolute", Path::isAbsolute)

/**
 * Maps this assertion to an assertion on the parent path or `null` if the subject does not have a parent.
 *
 * @see Path.getParent
 */
val <T : Path> Builder<T>.parent: Builder<Path?>
  get() = get("parent", Path::getParent)

/**
 * Maps this assertion to an assertion of this subject resolved with the provided path.
 *
 * @param other the path to resolve against this subject's path
 * @see Path.resolve
 */
infix fun <T : Path> Builder<T>.resolve(other: Path): Builder<Path> =
  get("resolved against $other") { resolve(other) }

/**
 * Maps this assertion to an assertion of this subject resolved with the provided path.
 *
 * @param other the path string to resolve against this subject's path
 * @see Path.resolve
 */
infix fun <T : Path> Builder<T>.resolve(other: String): Builder<Path> =
  get("resolved against $other") { resolve(other) }

/**
 * Asserts that the subject starts with the provided path.
 *
 * @param other the given path
 * @see Path.startsWith
 */
infix fun <T : Path> Builder<T>.startsWith(other: Path): Builder<T> =
  assertThat("starts with %s", expected = other) { it.startsWith(other) }

/**
 * Asserts that the subject starts with the provided path.
 *
 * @param other the given path string
 * @see Path.startsWith
 */
infix fun <T : Path> Builder<T>.startsWith(other: String): Builder<T> =
  assertThat("starts with %s", expected = other) { it.startsWith(other) }

/**
 * Maps this assertion to an assertion on the file object representing this subject.
 *
 * @see Path.toFile
 */
fun <T : Path> Builder<T>.toFile(): Builder<File> =
  get("as File", Path::toFile)

// java.nio.file.Files
/**
 * Asserts that the subject exists, handling symbolic links according to the provided [options]
 *
 * @param options the options indicating how symbolic links are handled
 * @see Files.exists
 */
fun <T : Path> Builder<T>.exists(vararg options: LinkOption = emptyArray()): Builder<T> =
  assertThat(descriptionForOptions("exists", options)) { Files.exists(it, *options) }

/**
 * Asserts that the subject is a directory, handling symbolic links according to the provided [options].
 *
 * @param options the options indicating how symbolic links are handled
 * @see Files.isDirectory
 */
fun <T : Path> Builder<T>.isDirectory(vararg options: LinkOption = emptyArray()): Builder<T> =
  assertThat(descriptionForOptions("is directory", options)) { Files.isDirectory(it, *options) }

/**
 * Asserts that the subject is executable link.
 *
 * @see Files.isExecutable
 */
fun <T : Path> Builder<T>.isExecutable(): Builder<T> =
  assertThat("is executable") { Files.isExecutable(it) }

/**
 * Asserts that the subject is readable.
 *
 * @see Files.isReadable
 */
fun <T : Path> Builder<T>.isReadable(): Builder<T> =
  assertThat("is readable") { Files.isReadable(it) }

/**
 * Asserts that the subject is a regular file, handling symbolic links according to the provided [options].
 *
 * @param options the options indicating how symbolic links are handled
 * @see Files.isRegularFile
 */
fun <T : Path> Builder<T>.isRegularFile(vararg options: LinkOption = emptyArray()): Builder<T> =
  assertThat(descriptionForOptions("is regular file", options)) { Files.isRegularFile(it, *options) }

/**
 * Asserts that the subject is a symbolic link.
 *
 * @see Files.isSymbolicLink
 */
fun <T : Path> Builder<T>.isSymbolicLink(): Builder<T> =
  assertThat("is symbolic lnk") { Files.isSymbolicLink(it) }

/**
 * Maps this assertion to an assertion over all bytes of this subject path .
 *
 * @see Files.readAllBytes
 */
fun <T : Path> Builder<T>.allBytes(): Builder<ByteArray> =
  get("all bytes", Files::readAllBytes)

/**
 * Maps this assertion to an assertion over all lines of this subject path decoded using the provided [charset].
 *
 * @param charset the charset to use in decoding
 * @see Files.readAllLines
 */
fun <T : Path> Builder<T>.allLines(charset: Charset = Charsets.UTF_8): Builder<List<String>> =
  get("all lines ($charset encoding)") { Files.readAllLines(this, charset) }

/**
 * Maps this assertion to an assertion over the byte size of the subject path.
 *
 * @see Files.size
 */
val <T : Path> Builder<T>.size: Builder<Long>
  get() = get("size (in bytes)", Files::size)

/**
 * Converts a description to another description
 * Useful is methods that take an optional array of [LinkOption]
 */
private fun descriptionForOptions(description: String, options: Array<out LinkOption>): String =
  if (options.isNotEmpty()) {
    "$description with options ${options.contentToString()}"
  } else {
    description
  }
