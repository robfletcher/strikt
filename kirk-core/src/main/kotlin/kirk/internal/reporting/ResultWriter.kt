package kirk.internal.reporting

import kirk.api.Result
import java.io.StringWriter

internal interface ResultWriter {
  fun writeTo(writer: Appendable, result: Result)
  fun writeTo(writer: Appendable, results: Iterable<Result>) =
    results.forEach { writeTo(writer, it) }
}

internal fun Result.writeToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writeTo(writer, this)
      writer.toString()
    }

internal fun Iterable<Result>.writeToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writeTo(writer, this)
      writer.toString()
    }