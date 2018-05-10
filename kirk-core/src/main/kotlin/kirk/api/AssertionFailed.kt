package kirk.api

import kirk.internal.reporting.writeToString

/**
 * Thrown to indicate an assertion, or a group or chain of assertions has
 * failed.
 *
 * @property results the results of the individual assertions that were
 * evaluated.
 * @property assertionCount the total number of assertions evaluated.
 * @property passCount the number of assertions that passed.
 * @property failureCount the number of assertions that failed.
 * @property message the human-readable assertion description.
 */
class AssertionFailed(
  val results: Collection<Result>
) : AssertionError() {
  /**
   * Creates an [AssertionFailed] exception with a single [result].
   */
  constructor(result: Result) : this(listOf(result))

  val assertionCount = results.sumBy { it.assertionCount }
  val passCount = results.sumBy { it.passCount }
  val failureCount = results.sumBy { it.failureCount }

  override val message: String by lazy { results.writeToString() }
}