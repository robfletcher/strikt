package strikt.assertions

import strikt.api.Assertion.Builder
import java.io.File
import java.net.URI
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
