package strikt.api

/**
 * Allows assertion implementations to assertAll [subject] and determine a
 * result.
 */
interface Assertion<T> {
  /**
   * The assertion subject.
   */
  val subject: T

  /**
   * Mark this result as passed.
   */
  fun pass()

  /**
   * Mark this result as failed.
   *
   * @param actual an optional actual value, that is the value that differed
   * from the expectation.
   * @param description A description of the failure. May contain a
   * [String.format] style placeholder for the [actual] value.
   * @property cause The exception that caused the failure, if any.
   */
  fun fail(
    actual: Any? = null,
    description: String? = if (actual == null) null else "found %s",
    cause: Throwable? = null
  )
}

/**
 * An assertion of a single condition on a subject.
 */
interface AtomicAssertion<T> : Assertion<T>

/**
 * An assertion composed of multiple conditions whose overall result is
 * determined by some aggregation of those conditions' results.
 *
 * @property results The results of any assertions in this sub-tree.
 * @property allPassed `true` if all composed assertions passed, otherwise
 * `false`.
 * @property anyPassed `true` if at least one composed assertion passed,
 * otherwise `false`.
 * @property allFailed `true` if all composed assertions failed, otherwise
 * `false`.
 * @property anyFailed `true` if at least one composed assertion failed,
 * otherwise `false`.
 */
interface CompoundAssertion<T> : Assertion<T> {
  val anyFailed: Boolean
//    get() = results.any { it.status is Failed }

  val allFailed: Boolean
//    get() = results.isNotEmpty() && results.all { it.status is Failed }

  val anyPassed: Boolean
//    get() = results.any { it.status is Passed }

  val allPassed: Boolean
//    get() = results.isNotEmpty() && results.all { it.status is Passed }
}
