package strikt.internal.reporting

import strikt.internal.ResultNode
import java.io.StringWriter

internal interface ResultWriter {
  fun writeTo(writer: Appendable, resultNode: ResultNode)
  fun writeTo(writer: Appendable, results: Iterable<ResultNode>) =
    results.forEach { writeTo(writer, it) }

  val verbose: Boolean
}

internal fun ResultNode.writeToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writeTo(writer, this)
      writer.toString()
    }

internal fun Iterable<ResultNode>.writeToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writeTo(writer, this)
      writer.toString()
    }
