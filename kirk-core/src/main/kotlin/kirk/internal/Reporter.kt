package kirk.internal

import java.io.StringWriter

internal interface Reporter {
  fun report(result: Result)
}

internal class FailFastReporter : Reporter {
  override fun report(result: Result) {
    StringWriter()
      .also(result::describeTo)
      .toString()
      .let(::println)
    if (result.status == Status.Failure) {
      throw AssertionFailed(result)
    }
  }
}

internal class AggregatingReporter : Reporter {
  private val _results = mutableListOf<Result>()

  override fun report(result: Result) {
    _results.add(result)
  }

  val results: List<Result>
    get() = _results

  val anyFailed: Boolean
    get() = _results.any { it.status == Status.Failure }

  val allFailed: Boolean
    get() = _results.all { it.status == Status.Failure }

  val anySucceeded: Boolean
    get() = _results.any { it.status == Status.Success }

  val allSucceeded: Boolean
    get() = _results.all { it.status == Status.Success }
}
