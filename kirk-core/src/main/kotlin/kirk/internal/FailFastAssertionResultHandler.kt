package kirk.internal

import kirk.api.AssertionFailed
import kirk.api.Result
import kirk.api.Status
import kirk.internal.reporting.AnsiColorResultWriter
import kirk.internal.reporting.writeToString

internal class FailFastAssertionResultHandler : AssertionResultHandler {
  override fun report(result: Result) {
    result.writeToString(AnsiColorResultWriter).let(::println)
    if (result.status == Status.Failed) {
      throw AssertionFailed(result)
    }
  }
}