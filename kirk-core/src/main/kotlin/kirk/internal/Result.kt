package kirk.internal

import kirk.api.Result
import kirk.api.Status

internal fun <T> result(status: Status, description: String, actual: T): Result =
  AtomicResult(status, description, actual)

internal fun <T> result(status: Status, description: String, actual: T, results: Iterable<Result>): Result =
  CompoundResult(status, description, actual, results)

internal data class AtomicResult(
  override val status: Status,
  override val description: String,
  override val subject: Any?
) : Result {
  override val assertionCount = 1
  override val passCount = when (status) {
    Status.Passed -> 1
    Status.Failed -> 0
  }
  override val failureCount = when (status) {
    Status.Passed -> 0
    Status.Failed -> 1
  }
}

internal data class CompoundResult(
  override val status: Status,
  override val description: String,
  override val subject: Any?,
  val results: Iterable<Result>
) : Result {
  override val assertionCount = results.sumBy { it.assertionCount }
  override val passCount = results.sumBy { it.passCount }
  override val failureCount = results.sumBy { it.failureCount }
}
