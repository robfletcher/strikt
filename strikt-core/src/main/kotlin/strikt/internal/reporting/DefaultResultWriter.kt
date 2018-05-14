package strikt.internal.reporting

import strikt.api.Reportable
import strikt.api.Result
import strikt.api.Status.*
import strikt.api.Subject

internal open class DefaultResultWriter : ResultWriter {
  override fun writeTo(writer: Appendable, result: Reportable) {
    writeIndented(writer, result)
  }

  private fun writeIndented(writer: Appendable, result: Reportable, indent: Int = 0) {
    writeLine(writer, result, indent)
    result.results.forEach {
      writeIndented(writer, it, indent + 1)
    }
  }

  protected open fun writeLine(writer: Appendable, result: Reportable, indent: Int) {
    when (result) {
      is Subject<*> -> result.writeSubject(writer, indent)
      is Result     -> result.writeResult(writer, indent)
    }
  }

  private fun Subject<*>.writeSubject(writer: Appendable, indent: Int) {
    writeLineStart(writer, this, indent)
    writer
      .append("▼ ")
      // TODO: handle without String.format
      .append(description.format(value))
    writeLineEnd(writer, this)
  }

  private fun Result.writeResult(writer: Appendable, indent: Int) {
    writeLineStart(writer, this, indent)
    writer
      .append(when (status) {
        Passed  -> "✔ "
        Failed  -> "✘ "
        Pending -> "? "
      })
      .append(description)
    writeLineEnd(writer, this)

    // TODO: recurse here, Actual should be just another Reportable
    writeActual(writer, indent)
  }

  private fun Result.writeActual(writer: Appendable, indent: Int) {
    actual?.let { actual ->
      writeLineStart(writer, this, indent + 1)
      writer
        .append("↳ ")
        // TODO: handle without String.format
        .append(actual.description.format(actual.value))
      writeLineEnd(writer, this)
    }
  }

  protected open fun writeLineStart(writer: Appendable, node: Reportable, indent: Int) {
    writer.append("".padStart(2 * indent))
  }

  protected open fun writeLineEnd(writer: Appendable, node: Reportable) {
    writer.append("\n")
  }
}
