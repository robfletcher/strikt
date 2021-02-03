package strikt.java

import strikt.api.Assertion.Builder
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Path

// java.io.File
/**
 * Maps this assertion to an assertion on the name of the file of the subject.
 *
 * @see File.getName
 */
val <T : File> Builder<T>.name: Builder<String>
  get() = get("name", File::getName)

/**
 * Maps this assertion to an assertion on the parent file or `null` if the subject does not have a parent.
 *
 * @see File.getParent
 */
val <T : File> Builder<T>.parent: Builder<String?>
  get() = get("parent", File::getParent)

/**
 * Maps this assertion to an assertion on the parent file or `null` if the subject does not have a parent.
 *
 * @see File.getParentFile
 */
val <T : File> Builder<T>.parentFile: Builder<File?>
  get() = get("parentFile", File::getParentFile)

/**
 * Maps this assertion to an assertion on a path object representing this subject.
 *
 * @see File.toPath
 */
fun <T : File> Builder<T>.toPath(): Builder<Path> =
  get("as Path", File::toPath)

// https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/java.io.-file/index.html
/**
 * Maps this assertion to an assertion on the file extension (not including the dot) or empty string if it not have one.
 *
 * @see File.extension
 */
val <T : File> Builder<T>.extension: Builder<String>
  get() = get("extension", File::extension)

/**
 * Maps this assertion to an assertion on the file name without the extension.
 *
 * @see File.nameWithoutExtension
 */
val <T : File> Builder<T>.nameWithoutExtension: Builder<String>
  get() = get("name without extension", File::nameWithoutExtension)

/**
 * Maps this assertion to an assertion on the [last modified][File.lastModified] of the subject.
 *
 * @see File.lastModified
 */
val <T : File> Builder<T>.lastModified: Builder<Long>
  get() = get(File::lastModified)

/**
 * Maps this assertion to an assertion on the [file size][File.length] of the subject.
 *
 * @see File.length
 */
val <T : File> Builder<T>.length: Builder<Long>
  get() = get(File::length)

/**
 * Maps this assertion to an assertion on the lines of the subject decoded using the provided [charset].
 *
 * @param charset the charset used to decode the content
 * @see readLines
 */
fun <T : File> Builder<T>.lines(charset: Charset = Charsets.UTF_8): Builder<List<String>> =
  get("lines (decoded with charset $charset)") { readLines(charset) }

/**
 * Maps this assertion to an assertion on the complete text of the subject decoded using the provided [charset].
 *
 * @param charset the charset used to decode the content
 * @see readText
 */
fun <T : File> Builder<T>.text(charset: Charset = Charsets.UTF_8): Builder<String> =
  get("text (decoded with charset $charset)") { readText(charset) }

/**
 * Maps this assertion to an assertion on the [child files][File.listFiles] of the subject.
 *
 * Note: In contrast to [File.listFiles], an empty list is returned for non-directory files.
 *
 * @see File.listFiles
 */
val <T : File> Builder<T>.childFiles: Builder<List<File>>
  get() = get("child files") { listFiles()?.toList() ?: emptyList() }

/**
 * Maps this assertion to an assertion on a specific child [name] of the subject.
 */
fun <T : File> Builder<T>.childFile(name: String): Builder<File> =
  get("child $name") { File(this, name) }

/**
 * Asserts that the file exists.
 *
 * @see File.exists
 */
fun <T : File> Builder<T>.exists(): Builder<T> =
  assert("exists") {
    when {
      it.exists() -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the file not exists.
 *
 * @see File.exists
 */
fun <T : File> Builder<T>.notExists(): Builder<T> =
  assert("not exists") {
    when {
      it.exists() -> fail()
      else -> pass()
    }
  }

/**
 * Asserts that the file is a regular file.
 *
 * @see File.isFile
 */
fun <T : File> Builder<T>.isRegularFile(): Builder<T> =
  assert("is a regular file") {
    when {
      it.isFile -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the file is not a regular file.
 *
 * @see File.isFile
 */
fun <T : File> Builder<T>.isNotRegularFile(): Builder<T> =
  assert("is not a regular file") {
    when {
      it.isFile -> fail()
      else -> pass()
    }
  }

/**
 * Asserts that the file is a directory.
 *
 * @see File.isDirectory
 */
fun <T : File> Builder<T>.isDirectory(): Builder<T> =
  assert("is a directory") {
    when {
      it.isDirectory -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the file is not a directory.
 *
 * @see File.isDirectory
 */
fun <T : File> Builder<T>.isNotDirectory(): Builder<T> =
  assert("is not a directory") {
    when {
      it.isDirectory -> fail()
      else -> pass()
    }
  }

/**
 * Asserts that the file is readable.
 *
 * @see File.canRead
 */
fun <T : File> Builder<T>.isReadable(): Builder<T> =
  assert("is readable") {
    when {
      it.canRead() -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the file is not readable.
 *
 * @see File.canRead
 */
fun <T : File> Builder<T>.isNotReadable(): Builder<T> =
  assert("is not readable") {
    when {
      it.canRead() -> fail()
      else -> pass()
    }
  }

/**
 * Asserts that the file is writable.
 *
 * @see File.canWrite
 */
fun <T : File> Builder<T>.isWritable(): Builder<T> =
  assert("is writable") {
    when {
      it.canWrite() -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the file is not writable.
 *
 * @see File.canWrite
 */
fun <T : File> Builder<T>.isNotWritable(): Builder<T> =
  assert("is not writable") {
    when {
      it.canWrite() -> fail()
      else -> pass()
    }
  }

/**
 * Asserts that the file is executable.
 *
 * @see File.canExecute
 */
fun <T : File> Builder<T>.isExecutable(): Builder<T> =
  assert("is executable") {
    when {
      it.canExecute() -> pass()
      else -> fail()
    }
  }

/**
 * Asserts that the file is not executable.
 *
 * @see File.canExecute
 */
fun <T : File> Builder<T>.isNotExecutable(): Builder<T> =
  assert("is not executable") {
    when {
      it.canExecute() -> fail()
      else -> pass()
    }
  }
