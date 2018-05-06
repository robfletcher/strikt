package kirk.internal

import kirk.api.Result
import kirk.api.Status
import kirk.internal.reporting.DefaultResultWriter
import java.io.StringWriter

internal class FailFastAssertionResultHandler : AssertionResultHandler {
  override fun report(result: Result) {
    StringWriter()
      .use {
        DefaultResultWriter(it).write(result)
        it.toString()
      }
      .let(::println)
    if (result.status == Status.Failed) {
      throw AssertionFailed(result)
    }
  }
}