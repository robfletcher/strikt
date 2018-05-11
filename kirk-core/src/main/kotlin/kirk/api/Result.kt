package kirk.api

import kirk.internal.Described

/**
 * Represents the result of an assertion or a group of assertions.
 *
 * @property status The status of the result.
 * @property description The description of the assertion as passed to
 * [Assertion.assert].
 * @property subject The subject value of the assertion.
 * @property actual The actual value or values that violated the assertion.
 * This property is optional as it does not make sense for all types of
 * assertion.
 * However, it can help improve diagnostic messages where it _is_ appropriate.
 * @property nestedResults Contains the results of any nested assertions.
 * @property assertionCount The number of assertions evaluated against [subject]
 * @property passCount The number of assertions that passed.
 * @property failureCount The number of assertions that failed.
 */
data class Result
internal constructor(
  val status: Status,
  val description: String,
  val subject: Described<Any?>,
  val actual: Described<Any?>? = null,
  val nestedResults: Collection<Result> = emptyList()
) {
  val assertionCount: Int = when (nestedResults.isEmpty()) {
    true  -> 1
    false -> nestedResults.size
  }

  val passCount: Int = when (nestedResults.isEmpty()) {
    true  -> if (status == Status.Passed) 1 else 0
    false -> nestedResults.sumBy { it.passCount }
  }

  val failureCount: Int = when (nestedResults.isEmpty()) {
    true  -> if (status == Status.Failed) 1 else 0
    false -> nestedResults.sumBy { it.failureCount }
  }
}
