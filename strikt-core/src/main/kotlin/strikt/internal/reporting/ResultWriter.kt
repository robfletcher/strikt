package strikt.internal.reporting

import strikt.internal.AssertionNode
import java.io.StringWriter

internal interface ResultWriter {
  fun writeTo(writer: Appendable, node: AssertionNode<*>)
  fun writeTo(writer: Appendable, results: Iterable<AssertionNode<*>>) =
    results.forEach { writeTo(writer, it) }
}

internal fun AssertionNode<*>.writeToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writeTo(writer, this)
      writer.toString()
    }

internal fun Iterable<AssertionNode<*>>.writeToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writeTo(writer, this)
      writer.toString()
    }
