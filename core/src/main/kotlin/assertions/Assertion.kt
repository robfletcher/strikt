package assertions

import java.io.StringWriter

interface Assertion<T> {
  fun evaluate(description: String, predicate: Reporter.(T) -> Unit)
}

internal class FailFastAssertion<T>(private val subject: T) : Assertion<T> {
  override fun evaluate(description: String, predicate: Reporter.(T) -> Unit) {
    val reporter = object : Reporter {
      override fun <A> aggregate(status: Status, actual: A, results: Iterable<Result>) {
        if (status === Status.Success) {
          Result.success(actual, description, results)
        } else {
          Result.failure(actual, description, results)
        }
          .also { result ->
            StringWriter()
              .also(result::describeTo)
              .toString()
              .let(::println)
            if (status === Status.Failure) {
              throw AssertionFailed(result)
            }
          }
      }

      override fun <A> report(status: Status, actual: A) {
        if (status === Status.Success) {
          Result.success(actual, description)
        } else {
          Result.failure(actual, description)
        }
          .also { result ->
            StringWriter()
              .also(result::describeTo)
              .toString()
              .let(::println)
            if (status === Status.Failure) {
              throw AssertionFailed(result)
            }
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
      override fun <A> aggregate(status: Status, actual: A, results: Iterable<Result>) {
        if (status === Status.Success) {
          Result.success(actual, description, results)
        } else {
          Result.failure(actual, description, results)
        }
          .also {
            _results.add(it)
          }
      }

      override fun <A> report(status: Status, actual: A) {
        if (status === Status.Success) {
          Result.success(actual, description)
        } else {
          Result.failure(actual, description)
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
