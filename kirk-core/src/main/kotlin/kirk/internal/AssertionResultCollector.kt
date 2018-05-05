package kirk.internal

import kirk.api.Result
import kirk.api.Status

internal class AssertionResultCollector : AssertionResultHandler {
  private val _results = mutableListOf<Result>()

  override fun report(result: Result) {
    _results.add(result)
  }

  val results: List<Result>
    get() = _results

  val anyFailed: Boolean
    get() = _results.any { it.status == Status.Failed }

  val allFailed: Boolean
    get() = _results.all { it.status == Status.Failed }

  val anyPassed: Boolean
    get() = _results.any { it.status == Status.Passed }

  val allPassed: Boolean
    get() = _results.all { it.status == Status.Passed }
}