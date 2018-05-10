package kirk.internal.reporting

import kirk.api.Result
import kirk.api.Status

internal open class DefaultResultWriter : ResultWriter {
  override fun writeTo(writer: Appendable, result: Result) {
    writeIndented(writer, result)
  }

  private fun writeIndented(writer: Appendable, result: Result, indent: Int = 0) {
    writeLine(writer, result, indent)
    result.nestedResults.forEach {
      writeIndented(writer, it, indent + 1)
    }
  }

  protected open fun writeLine(writer: Appendable, result: Result, indent: Int) {
    onLineStart(writer, result, indent)
    writer.append(when (result.status) {
      Status.Passed -> "✔ "
      Status.Failed -> "✘ "
    })
      .append(result.description.format(result.subject))
    onLineEnd(writer, result)
    if (result.actual != null) {
      onLineStart(writer, result, indent + 1)
      writer.append("↳ ")
        .append(result.actual.description.format(result.actual.value))
      onLineEnd(writer, result)
    }
  }

  protected open fun onLineStart(writer: Appendable, result: Result, indent: Int) {
    writer.append("".padStart(2 * indent))
  }

  protected open fun onLineEnd(writer: Appendable, result: Result) {
    writer.append("\n")
  }
}
