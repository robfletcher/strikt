package kirk.internal

import kirk.api.Result
import kirk.api.Status

internal class NegatedResultHandler(
  private val delegate: AssertionResultHandler
) : AssertionResultHandler {
  override fun report(result: Result) {
    delegate.report(result.copy(status = when (result.status) {
      Status.Passed -> Status.Failed
      Status.Failed -> Status.Passed
    }))
  }
}