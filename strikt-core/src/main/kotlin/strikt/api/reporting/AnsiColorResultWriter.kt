package strikt.api.reporting

import strikt.api.Status.*

internal object AnsiColorResultWriter : DefaultResultWriter() {
  override fun writeLineStart(writer: Appendable, node: Reportable, indent: Int) {
    super.writeLineStart(writer, node, indent)
    if (node is Result) {
      writer.append(when (node.status) {
        Passed  -> ANSI_GREEN
        Failed  -> ANSI_RED
        Pending -> ANSI_YELLOW
      })
    }
  }

  override fun writeLineEnd(writer: Appendable, node: Reportable) {
    if (node is Result) {
      writer.append(ANSI_RESET)
    }
    super.writeLineEnd(writer, node)
  }
}

private const val ANSI_GREEN = "\u001B[32m"
private const val ANSI_RED = "\u001B[31m"
private const val ANSI_YELLOW = "\u001B[33m"
private const val ANSI_RESET = "\u001B[0m"
