package kirk.api

import kirk.internal.reporting.writeToString

class AssertionFailed(
  val results: Collection<Result>
) : AssertionError() {
  constructor(result: Result) : this(listOf(result))

  val assertionCount = results.sumBy { it.assertionCount }
  val passCount = results.sumBy { it.passCount }
  val failureCount = results.sumBy { it.failureCount }

  override val message: String by lazy { results.writeToString() }
}