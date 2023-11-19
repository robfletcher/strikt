package strikt.internal.reporting

import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.AssertionNode

internal object AnsiColorResultWriter : DefaultResultWriter() {
  override fun writeLineStart(
    writer: Appendable,
    node: AssertionNode<*>,
    indent: Int
  ) {
    super.writeLineStart(writer, node, indent)
    writer.append(
      when (node.status) {
        is Passed -> ANSI_GREEN
        is Failed -> ANSI_RED
        is Pending -> ANSI_YELLOW
      }
    )
  }

  override fun writeLineEnd(
    writer: Appendable,
    node: AssertionNode<*>
  ) {
    writer.append(ANSI_RESET)
    super.writeLineEnd(writer, node)
  }
}

private const val ANSI_GREEN = "\u001B[32m"
private const val ANSI_RED = "\u001B[31m"
private const val ANSI_YELLOW = "\u001B[33m"
private const val ANSI_RESET = "\u001B[0m"
