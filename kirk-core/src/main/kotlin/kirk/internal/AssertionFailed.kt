package kirk.internal

import kirk.api.Result
import kirk.internal.reporting.writeToString

internal class AssertionFailed(
  private val results: Collection<Result>
) : AssertionError(
  results.writeToString()
) {
  constructor(result: Result) : this(listOf(result))

  val assertionCount = results.sumBy { it.assertionCount }
  val passCount = results.sumBy { it.passCount }
  val failureCount = results.sumBy { it.failureCount }
}