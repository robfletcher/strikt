package strikt.internal.peek

import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

data class FileInfo(
  val lineNumber: Int,
  val sourceFileName: String,
  val line: String
)

object FilePeek {
  fun getCallerFileInfo(offset: Int = 2): FileInfo {
    val callerStackTraceElement = RuntimeException().stackTrace[offset]
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
    val sourceFileName =
      if (File(sourceFileWithoutExtension.plus(".kt")).exists()) sourceFileWithoutExtension.plus(
        ".kt"
      ) else sourceFileWithoutExtension.plus(
        ".java"
      ).replace("src/test/kotlin", "src/test/java")

    val reader = try {
      FileReader(sourceFileName)
    } catch (e: FileNotFoundException) {
      throw RuntimeException("did not find source file for class file $classFilePath")
    }
    val callerLine = reader.useLines {
      it.drop(callerStackTraceElement.lineNumber - 1).first()
    }

    return FileInfo(
      callerStackTraceElement.lineNumber,
      sourceFileName = sourceFileName,
      line = callerLine.trim()
    )
  }
}
