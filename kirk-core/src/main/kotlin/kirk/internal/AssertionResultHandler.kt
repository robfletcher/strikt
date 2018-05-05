package kirk.internal

import kirk.api.Result

internal interface AssertionResultHandler {
  fun report(result: Result)
}
