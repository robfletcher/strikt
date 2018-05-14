package strikt.internal.reporting

import strikt.api.Reportable

internal object MarkdownResultWriter : DefaultResultWriter() {
  override fun writeLineStart(writer: Appendable, node: Reportable, indent: Int) {
    super.writeLineStart(writer, node, indent)
    writer.append("* ")
  }
}