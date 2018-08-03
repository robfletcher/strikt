package strikt.internal.reporting

import strikt.internal.ResultNode

internal object MarkdownResultWriter : DefaultResultWriter() {
  override fun writeLineStart(writer: Appendable, node: ResultNode, indent: Int) {
    super.writeLineStart(writer, node, indent)
    writer.append("* ")
  }
}
