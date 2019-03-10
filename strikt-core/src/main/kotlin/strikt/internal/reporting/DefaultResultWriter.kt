package strikt.internal.reporting

import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.AssertionChain
import strikt.internal.AssertionGroup
import strikt.internal.AssertionNode
import strikt.internal.AssertionResult
import strikt.internal.AssertionSubject

internal open class DefaultResultWriter : ResultWriter {

  @Suppress("PlatformExtensionReceiverOfInline")

  override fun writeTo(writer: Appendable, node: AssertionNode<*>) {
    writeIndented(writer, node)
  }

  override fun writePathTo(writer: Appendable, node: AssertionNode<*>) {
    val tree = mutableListOf<AssertionNode<*>>()
    node.addAncestorsTo(tree)

    tree.listIterator().also { iterator ->
      while (iterator.hasNext()) {
        val indent = iterator.nextIndex()
        val n = iterator.next()
        writeLine(writer, n, indent)
        writeLineEnd(writer, n)
      }
    }

    writeIndented(writer, node, tree.size)
  }

  private fun <S> AssertionNode<S>.addAncestorsTo(tree: MutableList<AssertionNode<*>>) {
    parent?.also {
      tree.add(0, it)
      it.addAncestorsTo(tree)
    }
  }

  private fun writeIndented(
    writer: Appendable,
    node: AssertionNode<*>,
    indent: Int = 0
  ) {
    writeLine(writer, node, indent)
    if (node is AssertionGroup<*>) {
      node.children.forEach {
        writeLineEnd(writer, it)
        writeIndented(
          writer,
          it,
          if (node is AssertionChain) indent else indent + 1
        )
      }
    }
  }

  protected open fun writeLine(
    writer: Appendable,
    node: AssertionNode<*>,
    indent: Int
  ) {
    when (node) {
      is AssertionSubject<*> ->
        node.writeSubject(writer, indent)
      is AssertionResult<*> ->
        node.writeResult(writer, indent)
    }
  }

  private fun AssertionSubject<*>.writeSubject(
    writer: Appendable,
    indent: Int
  ) {
    writeLineStart(writer, this, indent)
    writeSubjectIcon(writer)
    if (isRoot) {
      writer.append("Expect that ")
    }
    writer
      .append(description.format(formatValue(subject)))
      .append(":")
  }

  private fun AssertionResult<*>.writeResult(writer: Appendable, indent: Int) {
    writeLineStart(writer, this, indent)
    writeStatusIcon(writer, this)

    val failed = status as? Failed
    when {
      failed?.comparison != null -> {
        val formattedComparison = failed.comparison.formatValues()
        writer
          .append(description.format(formattedComparison.expected))
          .append(" : ")
          .append(
            (failed.description
              ?: "found %s").format(formattedComparison.actual)
          )
      }
      failed?.description != null -> writer
        .append(description.format(formatValue(expected)))
        .append(" : ")
        .append(failed.description)
      else -> writer.append(description.format(formatValue(expected)))
    }
  }

  protected open fun writeLineStart(
    writer: Appendable,
    node: AssertionNode<*>,
    indent: Int
  ) {
    if (node !is AssertionChain) {
      writer.append("".padStart(2 * indent))
    }
  }

  protected open fun writeLineEnd(writer: Appendable, node: AssertionNode<*>) {
    if (node !is AssertionChain) {
      writer.append(EOL)
    }
  }

  protected open fun writeStatusIcon(
    writer: Appendable,
    node: AssertionNode<*>
  ) {
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
}

private val EOL = System.getProperty("line.separator")
