package assertions

interface Reporter {

  fun <A> aggregate(status: Status, actual: A, results: Iterable<Result>)
  fun <A> report(status: Status, actual: A)
  fun <A> success(actual: A, results: Iterable<Result>) {
    aggregate(Status.Success, actual, results)
  }

  fun <A> failure(actual: A, results: Iterable<Result>) {
    aggregate(Status.Failure, actual, results)
  }

  fun <A> success(actual: A) {
    report(Status.Success, actual)
  }

  fun <A> failure(actual: A) {
    report(Status.Failure, actual)
  }
}