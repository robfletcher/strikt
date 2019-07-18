package strikt.internal.peek

import java.io.File

internal data class FileInfo(
  val lineNumber: Int,
  val sourceFileName: String,
  val line: String
)

internal class FilePeek {
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

    val sourceFile = sequenceOf("src/test/kotlin", "src/test/java")
      .map { sourceRoot ->
        val sourceFileWithoutExtension =
          classFilePath.replace(buildDir, sourceRoot)
            .plus("/" + className.replace(".", "/"))

        File(sourceFileWithoutExtension).parentFile
          .resolve(callerStackTraceElement.fileName)
      }.single(File::exists)

    val callerLine = sourceFile.bufferedReader().useLines { lines ->
      var braceDelta = 0
      lines.drop(callerStackTraceElement.lineNumber - 1)
        .takeWhileInclusive { line ->
          val openBraces = line.count { it == '{' }
          val closeBraces = line.count { it == '}' }
          braceDelta += openBraces - closeBraces
          braceDelta != 0
        }.map { it.trim() }.joinToString(separator = "")
    }

    return FileInfo(
      callerStackTraceElement.lineNumber,
      sourceFileName = sourceFile.absolutePath,
      line = callerLine.trim()
    )
  }
}

internal fun <T> Sequence<T>.takeWhileInclusive(pred: (T) -> Boolean): Sequence<T> {
  var shouldContinue = true
  return takeWhile {
    val result = shouldContinue
    shouldContinue = pred(it)
    result
  }
}

internal class SourceFileNotFoundException(classFilePath: String) :
  java.lang.RuntimeException("did not find source file for class file $classFilePath")
