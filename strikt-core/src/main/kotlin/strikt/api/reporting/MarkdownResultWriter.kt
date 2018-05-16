package strikt.api.reporting

internal object MarkdownResultWriter : DefaultResultWriter() {
  override fun writeLineStart(writer: Appendable, node: Reportable, indent: Int) {
    super.writeLineStart(writer, node, indent)
    writer.append("* ")
  }
}