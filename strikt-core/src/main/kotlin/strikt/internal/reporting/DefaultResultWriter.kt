package strikt.internal.reporting

import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.AssertionGroup
import strikt.internal.AssertionNode
import strikt.internal.AssertionResult
import strikt.internal.AssertionSubject
import strikt.internal.DescribedNode

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
      if (it is DescribedNode<*>) {
        tree.add(0, it)
      }
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
          if (node is DescribedNode) indent + 1 else indent
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
    // if the value spans > 1 line, this is how much to indent following lines
    val valueIndent =
      (description.indexOf("%")).coerceAtLeast(0) + 14 + (indent * 2)
    val formattedValue = formatValue(subject)
    writer
      .append(
        description.format(formattedValue).lines().joinToString(
          "\n${"".padStart(
            valueIndent
          )}|"
        )
      )
      .append(":")
  }

  private fun AssertionResult<*>.writeResult(writer: Appendable, indent: Int) {
    writeLineStart(writer, this, indent)
    writeStatusIcon(writer, this)

    val failed = status as? Failed
    when {
      failed?.comparison != null -> {
        val formattedComparison = failed.comparison.formatValues()
        val failedDescription = failed.description ?: "found %s"
        val descriptionIndent = description.indexOf("%")
        val descriptionIndentFollowing = descriptionIndent + (indent * 2) + 2
        // the amount to further indent the "found %s" line so the values line up
        val alignIndent =
          (descriptionIndent - failedDescription.indexOf("%") + 2)
            .coerceAtLeast(2)
        val alignIndentFollowing =
          alignIndent + failedDescription.indexOf("%") + (indent * 2)
        writer
          .append(
            description.format(formattedComparison.expected).let {
              val lines = it.lines()
              if (lines.size > 1) {
                lines
                  .joinToString("\n${"".padStart(descriptionIndentFollowing)}|")
              } else {
                it
              }
            }
          )
          .append("\n")
        writeLineStart(writer, this, indent)
        writer
          .append("".padEnd(alignIndent))
          .append(
            failedDescription.format(formattedComparison.actual).let {
              val lines = it.lines()
              if (lines.size > 1) {
                lines.joinToString("\n${"".padStart(alignIndentFollowing)}|")
              } else {
                it
              }
            }
          )
      }
      failed?.description != null ->
        writer
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
    if (node is DescribedNode) {
      writer.append("".padStart(2 * indent))
    }
  }

  protected open fun writeLineEnd(writer: Appendable, node: AssertionNode<*>) {
    if (node is DescribedNode) {
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
