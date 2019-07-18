package strikt.internal

import filepeek.FilePeek

internal object FilePeek {
  val filePeek by lazy {
    FilePeek(
      listOf(
        "strikt.internal",
        "strikt.api",
        "filepeek"
      )
    )
  }
}
