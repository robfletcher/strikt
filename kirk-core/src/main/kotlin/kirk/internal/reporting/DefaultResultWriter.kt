package kirk.internal.reporting

import kirk.api.Result
import kirk.api.Status
import kirk.internal.CompoundResult

internal class DefaultResultWriter(
  private val writer: Appendable
) : ResultWriter {
  override fun write(result: Result) {
    writeIndented(result)
  }

  private fun writeIndented(result: Result, indent: Int = 0) {
    writer
      .append("".padStart(indent))
      .append(when (result.status) {
        Status.Passed -> "✔ "
        Status.Failed -> "✘ "
      })
      .append("${result.subject} ${result.description}")

    // TODO: no
    if (result is CompoundResult) {
      writer.append(":\n")
      result.results.forEach {
        writeIndented(it, indent + 2)
      }
    } else {
      writer.append("\n")
    }
  }
}