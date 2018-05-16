package strikt.api

import strikt.api.reporting.Reportable
import strikt.api.reporting.writeToString

/**
 * Thrown to indicate an assertion, or a group or chain of assertions has
 * failed.
 *
 * @property message the human-readable assertion description.
 */
class AssertionFailed
internal constructor(
  val subject: Reportable
) : AssertionError() {
  override val message: String by lazy { subject.writeToString() }
}
