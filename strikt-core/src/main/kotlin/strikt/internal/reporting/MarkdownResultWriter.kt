package strikt.internal.reporting

import strikt.internal.AssertionNode

internal object MarkdownResultWriter : DefaultResultWriter() {
  override fun writeLineStart(
    writer: Appendable,
    node: AssertionNode<*>,
    indent: Int
  ) {
    super.writeLineStart(writer, node, indent)
    writer.append("* ")
  }
}
