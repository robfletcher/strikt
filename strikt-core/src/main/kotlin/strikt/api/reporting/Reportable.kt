package strikt.api.reporting

import strikt.api.Failure
import strikt.api.Status
import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending

/**
 * Part of a graph of assertion results.
 *
 * @property status The status of the assertion or group of assertions.
 * @property root The root of the assertion graph.
 * @property results The results of any assertions in this node of the graph.
 * @property allPassed `true` if all composed assertions passed, otherwise
 * `false`.
 * @property anyPassed `true` if at least one composed assertion passed,
 * otherwise `false`.
 * @property allFailed `true` if all composed assertions failed, otherwise
 * `false`.
 * @property anyFailed `true` if at least one composed assertion failed,
 * otherwise `false`.
 */
sealed class Reportable {
  abstract val status: Status
  private var parent: Reportable? = null

  val root: Reportable
    get() = parent.let {
      when (it) {
        null -> this
        else -> it.root
      }
    }

  val results: Collection<Reportable>
    get() = _results

  /**
   * Append a new result to this node in the graph.
   */
  fun append(result: Reportable) {
    result.parent = this
    _results.add(result)
  }

  val anyFailed: Boolean
    get() = results.any { it.status == Failed }

  val allFailed: Boolean
    get() = results.isNotEmpty() && results.all { it.status == Failed }

  val anyPassed: Boolean
    get() = results.any { it.status == Passed }

  val allPassed: Boolean
    get() = results.isNotEmpty() && results.all { it.status == Passed }

  private val _results = mutableListOf<Reportable>()
}

/**
 * THe subject of an assertion.
 *
 * @property value The subject value.
 * @property description The formattable description of the subject.
 */
data class Subject<T>(
  val value: T,
  val description: String = "%s"
) : Reportable() {
  override val status: Status
    get() = when {
      results.all { it.status == Passed } -> Passed
      results.all { it.status == Pending } -> Pending
      else -> Failed
    }
}

/**
 * Represents the result of an assertion or a group of assertions.
 *
 * @property status The status of the result.
 * @property description The description of the assertion as passed to
 * [strikt.api.Assertion.assert].
 * @property actual The actual value or values that violated the assertion.
 * This property is optional as it does not make sense for all types of
 * assertion.
 * However, it can help improve diagnostic messages where it _is_ appropriate.
 * @property results Contains the results of any nested assertions.
 * @property actual The actual value, if relevant to the assertion.
 * @property expected The actual value, if relevant to the assertion.
 * @property message A description of the failure.
 * @property cause The exception that caused the failure, if any.
 */
data class Result
internal constructor(
  val description: String,
  val expected: Any?
) : Reportable() {
  private var _status: Status = Pending
  private var failure: Failure? = null

  /**
   * Mark this result as passed.
   */
  fun pass() {
    _status = Passed
  }

  /**
   * Mark this result as failed.
   *
   * @param failure A description of the failure.
   */
  fun fail(failure: Failure? = null) {
    _status = Failed
    this.failure = failure
  }

  override val status: Status
    get() = _status

  val actual: Any?
    get() = failure?.actual

  val message: String?
    get() = failure?.message

  val cause: Throwable?
    get() = failure?.cause
}
