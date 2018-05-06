package kirk.internal.reporting

import kirk.api.Result

internal object MarkdownResultWriter : DefaultResultWriter() {
  override fun onLineStart(writer: Appendable, result: Result, indent: Int) {
    super.onLineStart(writer, result, indent)
    writer.append("* ")
  }
}