package kirk.internal

import kirk.api.Result
import kirk.api.Status
import java.io.StringWriter

internal class FailFastAssertionResultHandler : AssertionResultHandler {
  override fun report(result: Result) {
    StringWriter()
      .also(result::describeTo)
      .toString()
      .let(::println)
    if (result.status == Status.Failed) {
      throw AssertionFailed(result)
    }
  }
}