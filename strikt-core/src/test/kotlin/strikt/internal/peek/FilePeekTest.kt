package strikt.internal.peek

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.endsWith
import strikt.assertions.isEqualTo

class FilePeekTest {
  @Test
  fun `can get FileInfo`() {
    val fileInfo = FilePeek.getCallerFileInfo()

    expect(
      fileInfo
    ) {
      map(FileInfo::sourceFileName)
        .endsWith("src/test/kotlin/strikt/internal/peek/FilePeekTest.kt")
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
        .endsWith("src/test/kotlin/strikt/internal/peek/FilePeekTest.kt")
      map(FileInfo::line)
        .isEqualTo("val fileInfo = { FilePeek.getCallerFileInfo() }()")
    }
  }
}

class FilePeekTestWithDifferentNameThanItsFile {
  @Test
  @Disabled("this is currently broken")
  fun `finds classes that have a different name than the file they are in`() {
    FilePeek.getCallerFileInfo()
  }
}
