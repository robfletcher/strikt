package assertions

interface Reporter {

  fun aggregate(status: Status, results: Iterable<Result>)

  fun report(status: Status)

  fun success(results: Iterable<Result>) {
    aggregate(Status.Success, results)
  }

  fun failure(results: Iterable<Result>) {
    aggregate(Status.Failure, results)
  }

  fun success() {
    report(Status.Success)
  }

  fun failure() {
    report(Status.Failure)
  }
}