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
}
class FilePeekTestWithDifferentNameThanItsFile {
  @Test
  fun `finds classes that have a different name than the file they are in`() {
    FilePeek.getCallerFileInfo()
  }
}
