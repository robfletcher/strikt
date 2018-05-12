package kirk.api

import kirk.internal.reporting.writeToString

/**
 * Thrown to indicate an assertion, or a group or chain of assertions has
 * failed.
 *
 * @property result the result of the assertions that were evaluated.
 * @property assertionCount the total number of assertions evaluated.
 * @property passCount the number of assertions that passed.
 * @property failureCount the number of assertions that failed.
 * @property message the human-readable assertion description.
 */
class AssertionFailed(
  val subject: Subject<*>
) : AssertionError() {

  val assertionCount = subject.assertionCount
  val passCount = subject.passCount
  val failureCount = subject.failureCount

  override val message: String by lazy { subject.writeToString() }
}
