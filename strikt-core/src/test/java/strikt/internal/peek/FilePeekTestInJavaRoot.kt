package strikt.internal.peek

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class FilePeekTestInJavaRoot {
  @Test
  fun `finds classes that have a different name than the file they are in`() {
    expectThat(FilePeek().getCallerFileInfo(filterMethod("finds")))
      .get { line }
      .isEqualTo("expectThat(FilePeek().getCallerFileInfo(filterMethod(\"finds\")))")
  }
}
