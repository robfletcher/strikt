package assertions

import java.io.StringWriter

interface Reporter {
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

class AggregatingReporter : Reporter {
  private val _results = mutableListOf<Result>()

  override fun report(result: Result) {
    _results.add(result)
  }

  val results: List<Result>
    get() = _results
}
