package kirk.internal

import kirk.api.Result

internal interface Reporter {
  fun report(result: Result)
}
