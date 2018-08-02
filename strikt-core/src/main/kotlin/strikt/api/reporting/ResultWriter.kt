package strikt.api.reporting

import java.io.StringWriter

internal interface ResultWriter {
  fun writeTo(writer: Appendable, result: Reportable)
  fun writeTo(writer: Appendable, results: Iterable<Reportable>) =
    results.forEach { writeTo(writer, it) }

  val verbose: Boolean
}

internal fun Reportable.writeToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writeTo(writer, this)
      writer.toString()
    }

internal fun Iterable<Reportable>.writeToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writeTo(writer, this)
      writer.toString()
    }
