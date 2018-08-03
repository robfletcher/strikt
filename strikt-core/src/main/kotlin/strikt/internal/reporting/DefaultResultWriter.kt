package strikt.internal.reporting

import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.AssertionResult
import strikt.internal.AssertionSubject
import strikt.internal.ResultNode

internal open class DefaultResultWriter : ResultWriter {

  @Suppress("PlatformExtensionReceiverOfInline")
  override val verbose: Boolean
    get() = System.getProperty("strikt.verbose", "false").toBoolean()

  override fun writeTo(writer: Appendable, resultNode: ResultNode) {
    if (resultNode.shouldWrite) {
      writeIndented(writer, resultNode)
    }
  }

  private fun writeIndented(
    writer: Appendable,
    resultNode: ResultNode,
    indent: Int = 0
  ) {
    writeLine(writer, resultNode, indent)
    resultNode.children.forEach {
      if (it.shouldWrite) {
        writeLineEnd(writer, it)
        writeIndented(writer, it, indent + 1)
      }
    }
  }

  protected open fun writeLine(
    writer: Appendable,
    resultNode: ResultNode,
    indent: Int
  ) {
    when (resultNode) {
      is AssertionResult<*> ->
        resultNode.writeResult(writer, indent)
      is AssertionSubject<*> ->
        resultNode.writeSubject(writer, indent)
    }
  }

  private fun AssertionSubject<*>.writeSubject(writer: Appendable, indent: Int) {
    writeLineStart(writer, this, indent)
    writeSubjectIcon(writer)
    writer
      .append("Expect that ")
      .append(subject.describe(::formatValue))
      .append(":")
  }

  private fun AssertionResult<*>.writeResult(writer: Appendable, indent: Int) {
    writeLineStart(writer, this, indent)
    writeStatusIcon(writer, this)
    val (formattedExpected, formattedActual) =
      formatValues(expected, (status as? Failed)?.actual)
    writer.append(description.format(formattedExpected, formattedActual))
  }

  protected open fun writeLineStart(
    writer: Appendable,
    node: ResultNode,
    indent: Int
  ) {
    writer.append("".padStart(2 * indent))
  }

  protected open fun writeLineEnd(writer: Appendable, node: ResultNode) {
    writer.append(EOL)
  }

  protected open fun writeStatusIcon(writer: Appendable, node: ResultNode) {
    writer.append(
      when (node.status) {
        is Passed -> "✓ "
        is Failed -> "✗ "
        is Pending -> "? "
      }
    )
  }

  protected open fun writeSubjectIcon(writer: Appendable) {
    writer.append("▼ ")
  }

  private val ResultNode.shouldWrite: Boolean
    get() = status != Passed || verbose
}

private val EOL = System.getProperty("line.separator")
