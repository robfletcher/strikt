package strikt.test

import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.endsWith
import strikt.assertions.isEqualTo
import strikt.internal.peek.FileInfo
import strikt.internal.peek.FilePeek

/*
 * i moved this file to a different package to avoid the package filtering
 */
class FilePeekTest {
  @Test
  fun `can get FileInfo`() {
    val fileInfo = FilePeek.getCallerFileInfo()

    expect(
      fileInfo
    ) {
      map(FileInfo::sourceFileName)
        .endsWith("src/test/kotlin/strikt/test/FilePeekTest.kt")
      map(FileInfo::line)
        .isEqualTo("val fileInfo = FilePeek.getCallerFileInfo()")
    }
  }

  @Test
  fun `can get FileInfo for a block`() {
    val fileInfo = { FilePeek.getCallerFileInfo() }()

    expect(
      fileInfo
    ) {
      map(FileInfo::sourceFileName)
        .endsWith("src/test/kotlin/strikt/test/FilePeekTest.kt")
      map(FileInfo::line)
        .isEqualTo("val fileInfo = { FilePeek.getCallerFileInfo() }()")
    }
  }

  @Test
  fun `can get block body even when it contains multiple `() {
    fun mapMethod(@Suppress("UNUSED_PARAMETER") block: () -> Unit) =
      FilePeek.getCallerFileInfo { it.methodName.startsWith("can get") }

    val fileInfo = mapMethod {
      /* LOL! I'm a block body*/
      listOf(1, 2, 3).map { it }
    }

    expect(fileInfo).map(FileInfo::line)
      .isEqualTo("val fileInfo = mapMethod {/* LOL! I'm a block body*/listOf(1, 2, 3).map { it }}")
  }
}

class FilePeekTestWithDifferentNameThanItsFile {
  @Test
  fun `finds classes that have a different name than the file they are in`() {
    FilePeek.getCallerFileInfo()
  }
}
