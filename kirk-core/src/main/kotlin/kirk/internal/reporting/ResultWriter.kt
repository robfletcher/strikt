package kirk.internal.reporting

import kirk.api.Result
import java.io.StringWriter

internal interface ResultWriter {
  fun write(result: Result)
  fun write(results: Iterable<Result>) = results.forEach(::write)
}

fun Result.writeToString() =
  StringWriter()
    .use { writer ->
      DefaultResultWriter(writer).write(this)
      writer.toString()
    }

fun Iterable<Result>.writeToString() =
  StringWriter()
    .use { writer ->
      DefaultResultWriter(writer).write(this)
      writer.toString()
    }