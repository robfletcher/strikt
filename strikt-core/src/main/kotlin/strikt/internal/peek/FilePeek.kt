package strikt.internal.peek

import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

internal data class FileInfo(
  val lineNumber: Int,
  val sourceFileName: String,
  val line: String
)

internal object FilePeek {
  private val STRIKT_PACKAGES = listOf("strikt.internal", "strikt.api")

  fun getCallerFileInfo(
    filter: (StackTraceElement) -> Boolean = { el ->
      STRIKT_PACKAGES
        .none { el.className.startsWith(it) }
    }
  ): FileInfo {
    val stackTrace = RuntimeException().stackTrace

    val callerStackTraceElement = stackTrace.first(filter)
    val className = callerStackTraceElement.className.substringBefore('$')
    val clazz = javaClass.classLoader.loadClass(className)!!
    val classFilePath = File(clazz.protectionDomain.codeSource.location.path)
      .absolutePath

    val buildDir = when {
      classFilePath.contains("/out/") -> "out/test/classes" // running inside IDEA
      classFilePath.contains("build/classes/java") -> "build/classes/java/test" // gradle 4.x java source
      classFilePath.contains("build/classes/kotlin") -> "build/classes/kotlin/test" // gradle 4.x kotlin sources
      else -> "build/classes/test" // older gradle
    }

    val sourceFileWithoutExtension =
      classFilePath.replace(buildDir, "src/test/kotlin")
        .plus("/" + className.replace(".", "/"))
    val sourceFile = File(sourceFileWithoutExtension).parentFile
      .resolve(callerStackTraceElement.fileName)
    val reader = try {
      FileReader(sourceFile)
    } catch (e: FileNotFoundException) {
      throw SourceFileNotFoundException(classFilePath)
    }
    val callerLine = reader.useLines { lines ->
      var braces = 0
      lines.drop(callerStackTraceElement.lineNumber - 1)
        .takeWhileInclusive { line ->
          val openBraces = line.count { it == '{' }
          val closeBraces = line.count { it == '}' }
          braces += openBraces - closeBraces
          braces != 0
        }.map { it.trim() }.joinToString(separator = "")
    }

    return FileInfo(
      callerStackTraceElement.lineNumber,
      sourceFileName = sourceFile.absolutePath,
      line = callerLine.trim()
    )
  }
}

fun <T> Sequence<T>.takeWhileInclusive(pred: (T) -> Boolean): Sequence<T> {
  var shouldContinue = true
  return takeWhile {
    val result = shouldContinue
    shouldContinue = pred(it)
    result
  }
}

class SourceFileNotFoundException(classFilePath: String) :
  java.lang.RuntimeException("did not find source file for class file $classFilePath")
