package kirk.internal.reporting

import kirk.api.Result
import kirk.api.Status

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

    if (result.nestedResults.isNotEmpty()) writer.append(":")
    writer.append("\n")
    result.nestedResults.forEach {
      writeIndented(it, indent + 2)
    }
  }
}