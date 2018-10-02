package strikt.internal.peek

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.endsWith
import strikt.assertions.isEqualTo

class FilePeekTest {
  private val fileName = "src/test/kotlin/strikt/internal/peek/FilePeekTest.kt"

  @Test
  fun `can get FileInfo`() {
    val fileInfo = FilePeek.getCallerFileInfo(filterMethod("can get"))

    expectThat(fileInfo) {
      get(FileInfo::sourceFileName)
        .endsWith(fileName)
      get(FileInfo::line)
        .isEqualTo("""val fileInfo = FilePeek.getCallerFileInfo(filterMethod("can get"))""")
    }
  }

  @Test
  fun `can get FileInfo for a block`() {
    val fileInfo = { FilePeek.getCallerFileInfo(filterMethod("can get")) }()

    expectThat(fileInfo) {
      get(FileInfo::sourceFileName)
        .endsWith(fileName)
      get(FileInfo::line)
        .isEqualTo("""val fileInfo = { FilePeek.getCallerFileInfo(filterMethod("can get")) }()""")
    }
  }

  @Test
  fun `can get block body even when it contains multiple `() {
    fun mapMethod(@Suppress("UNUSED_PARAMETER") block: () -> Unit) =
      FilePeek.getCallerFileInfo(filterMethod("can get"))

    val fileInfo = mapMethod {
      /* LOL! I'm a block body*/
      listOf(1, 2, 3).map { it }
    }

    expectThat(fileInfo).get(FileInfo::line)
      .isEqualTo("val fileInfo = mapMethod {/* LOL! I'm a block body*/listOf(1, 2, 3).map { it }}")
  }
}

fun filterMethod(methodName: String): (StackTraceElement) -> Boolean =
  { it.methodName.startsWith(methodName) }

class FilePeekTestWithDifferentNameThanItsFile {
  @Test
  fun `finds classes that have a different name than the file they are in`() {
    expectThat(FilePeek.getCallerFileInfo(filterMethod("finds")))
      .get { it.line }
      .isEqualTo("expectThat(FilePeek.getCallerFileInfo(filterMethod(\"finds\")))")
  }
}
