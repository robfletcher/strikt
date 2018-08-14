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
 */
interface CompoundAssertion : Assertion {
  val anyFailed: Boolean
  val allFailed: Boolean
  val anyPassed: Boolean
  val allPassed: Boolean
}
