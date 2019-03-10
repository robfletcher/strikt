package strikt.api

/**
 * An assertion composed of multiple conditions whose overall result is
 * determined by some aggregation of those conditions' results.
 *
 * @property allPassed `true` if all composed assertions passed, otherwise
 * `false`.
 * @property anyPassed `true` if at least one composed assertion passed,
 * otherwise `false`.
 * @property allFailed `true` if all composed assertions failed, otherwise
 * `false`.
 * @property anyFailed `true` if at least one composed assertion failed,
 * otherwise `false`.
 * @property passedCount the number of composed assertions whose status is
 * [strikt.api.Status.Passed].
 * @property failedCount the number of composed assertions whose status is
 * [strikt.api.Status.Failed].
 */
interface CompoundAssertion : Assertion {
  val anyFailed: Boolean
  val allFailed: Boolean
  val anyPassed: Boolean
  val allPassed: Boolean
  val passedCount: Int
  val failedCount: Int
}
