package kirk.api

import kirk.api.Status.*
import kirk.internal.Described

sealed class Reportable {
  abstract val status: Status
  private var parent: Reportable? = null

  val root: Reportable
    get() {
      var node = this
      while (node.parent != null) {
        node = node.parent!! // TODO: why do I need !!?
      }
      return node
    }

  val results: Collection<Reportable>
    get() = _results

  fun append(result: Reportable) {
    result.parent = this
    _results.add(result)
  }

  val anyFailed: Boolean
    get() = results.any { it.status == Failed }

  val allFailed: Boolean
    get() = results.all { it.status == Failed }

  val anyPassed: Boolean
    get() = results.any { it.status == Passed }

  val allPassed: Boolean
    get() = results.all { it.status == Passed }

  private val _results = mutableListOf<Reportable>()
}

// TODO: rename
data class Subject<T>(
  val description: String,
  val value: T
) : Reportable() {
  override val status: Status
    get() = when {
      results.all { it.status == Passed } -> Passed
      else                                -> Failed
    }
}

/**
 * Represents the result of an assertion or a group of assertions.
 *
 * @property status The status of the result.
 * @property description The description of the assertion as passed to
 * [Assertion.assert].
 * @property actual The actual value or values that violated the assertion.
 * This property is optional as it does not make sense for all types of
 * assertion.
 * However, it can help improve diagnostic messages where it _is_ appropriate.
 * @property results Contains the results of any nested assertions.
 */
// TODO: this should not have nested results
data class Result
internal constructor(val description: String? = null) : Reportable() {
  private var _status: Status = Pending
  private var _actual: Described<Any?>? = null

  fun pass() {
    _status = Passed
  }

  fun fail() {
    _status = Failed
  }

  fun fail(actualDescription: String, actualValue: Any?) {
    fail()
    _actual = Described(actualDescription, actualValue)
  }

  override val status: Status
    get() = _status

  val actual: Described<*>?
    get() = _actual
}
