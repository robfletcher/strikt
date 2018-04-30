package assertions

import java.io.StringWriter

interface Assertion<T> {
  fun evaluate(description: String, predicate: Reporter.(T) -> Unit)
}

internal class FailFastAssertion<T>(private val subject: T) : Assertion<T> {
  override fun evaluate(description: String, predicate: Reporter.(T) -> Unit) {
    val reporter = object : Reporter {
      override fun aggregate(status: Status, results: Iterable<Result>) {
        if (status === Status.Success) {
          Result.success(subject, description, results)
        } else {
          Result.failure(subject, description, results)
        }
          .also(this::logAndFail)
      }

      override fun report(status: Status) {
        if (status === Status.Success) {
          Result.success(subject, description)
        } else {
          Result.failure(subject, description)
        }
          .also(this::logAndFail)
      }

      private fun logAndFail(result: Result) {
        StringWriter()
          .also(result::describeTo)
          .toString()
          .let(::println)
        if (result is Failure) {
          throw AssertionFailed(result)
        }
      }
    }
    reporter.predicate(subject)
  }
}

internal class CollectingAssertion<T>(private val subject: T) : Assertion<T> {
  private val _results = mutableListOf<Result>()

  override fun evaluate(description: String, predicate: Reporter.(T) -> Unit) {
    val reporter = object : Reporter {
      override fun aggregate(status: Status, results: Iterable<Result>) {
        if (status === Status.Success) {
          Result.success(subject, description, results)
        } else {
          Result.failure(subject, description, results)
        }
          .also {
            _results.add(it)
          }
      }

      override fun report(status: Status) {
        if (status === Status.Success) {
          Result.success(subject, description)
        } else {
          Result.failure(subject, description)
        }
          .also {
            _results.add(it)
          }
      }
    }
    reporter.predicate(subject)
  }

  val results: List<Result>
    get() = _results
}
