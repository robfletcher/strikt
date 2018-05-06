package kirk.internal.reporting

import kirk.api.Result
import kirk.api.Status

internal object AnsiColorResultWriter : DefaultResultWriter() {
  override fun onLineStart(writer: Appendable, result: Result, indent: Int) {
    super.onLineStart(writer, result, indent)
    writer.append(when (result.status) {
      Status.Passed -> ANSI_GREEN
      Status.Failed -> ANSI_RED
    })
  }

  override fun onLineEnd(writer: Appendable, result: Result) {
    writer.append(ANSI_RESET)
    super.onLineEnd(writer, result)
  }
}

private const val ANSI_GREEN = "\u001B[32m"
private const val ANSI_RED = "\u001B[31m"
private const val ANSI_RESET = "\u001B[0m"
