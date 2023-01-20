package strikt.internal.reporting

import strikt.internal.AssertionNode
import java.io.StringWriter

internal interface ResultWriter {
  fun writeTo(writer: Appendable, node: AssertionNode<*>)

  fun writeTo(writer: Appendable, results: Iterable<AssertionNode<*>>) =
    results.forEachIndexed { index, it ->
      if (index > 0) writer.append(System.lineSeparator())
      writeTo(writer, it)
    }

  fun writePathTo(writer: Appendable, node: AssertionNode<*>)
}

internal fun AssertionNode<*>.writeToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writeTo(writer, this)
      writer.toString()
    }

internal fun AssertionNode<*>.writePartialToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writePathTo(writer, this)
      writer.toString()
    }

internal fun Iterable<AssertionNode<*>>.writeToString(resultWriter: ResultWriter = DefaultResultWriter()) =
  StringWriter()
    .use { writer ->
      resultWriter.writeTo(writer, this)
      writer.toString()
    }
