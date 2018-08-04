package strikt.internal

import org.opentest4j.TestSkippedException
import strikt.api.Assertion
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.Status
import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.opentest4j.AtomicAssertionFailure
import strikt.internal.opentest4j.CompoundAssertionFailure
import strikt.internal.reporting.writeToString

/**
 * Part of a graph of assertion results.
 *
 * @property status The status of the assertion or group of assertions.
 * @property root The root of the assertion graph.
 */
internal sealed class ResultNode(
  private val parent: ResultNode?
) {
  abstract val status: Status

  val root: ResultNode
    get() = parent?.root ?: this

  init {
    parent?.also { it._children.add(this) }
  }

  private val _children = mutableListOf<ResultNode>()
  val children: List<ResultNode>
    get() = _children

  fun throwOnFailure() {
    root.toError()?.let { throw it }
  }

  abstract fun toError(): Throwable?
}

internal class AssertionSubject<T>(
  parent: ResultNode?,
  var subject: Described<T>
) : ResultNode(parent) {
  constructor(value: T) : this(null, Described(value))
  constructor(parent: ResultNode, value: T) : this(parent, Described(value))

  override val status: Status
    get() = when {
      children.isEmpty() -> Pending
      children.any { it.status is Status.Pending } -> Pending
      children.any { it.status is Status.Failed } -> Failed()
      else -> Passed
    }

  override fun toError(): Throwable? = when (status) {
    is Failed -> CompoundAssertionFailure(writeToString(), children.toErrors())
    is Pending -> TestSkippedException(writeToString())
    is Passed -> null
  }
}

internal abstract class AssertionResult<T>(
  parent: ResultNode
) : ResultNode(parent), Assertion<T> {
  abstract val description: String
  abstract val expected: Any?

  private var _status: Status = Pending

  override val status: Status
    get() = _status

  override fun pass() {
    _status = Passed
  }

  override fun fail(actual: Any?, description: String?, cause: Throwable?) {
    _status = Failed(actual, description, cause)
  }
}

internal class AtomicAssertionResult<T>(
  parent: ResultNode,
  override val subject: T,
  override val description: String,
  override val expected: Any? = null
) : AssertionResult<T>(parent), AtomicAssertion<T> { // TODO: implement interface by delegation

  constructor(parent: AssertionSubject<T>, description: String, expected: Any?) :
    this(parent, parent.subject.value, description, expected)

  override fun toError(): Throwable? = when (status) {
    is Failed -> AtomicAssertionFailure(
      writeToString(),
      expected,
      (status as? Failed)?.actual,
      (status as? Failed)?.cause
    )
    is Pending -> TestSkippedException(writeToString())
    is Passed -> null
  }
}

internal class CompoundAssertionResult<T>(
  parent: ResultNode,
  override val subject: T,
  override val description: String,
  override val expected: Any? = null
) : AssertionResult<T>(parent), CompoundAssertion<T> { // TODO: implement interface by delegation

  constructor(parent: AssertionSubject<T>, description: String, expected: Any?) :
    this(parent, parent.subject.value, description, expected)

  override val anyFailed: Boolean
    get() = children.any { it.status is Failed }
  override val allFailed: Boolean
    get() = children.all { it.status is Failed }
  override val anyPassed: Boolean
    get() = children.any { it.status is Passed }
  override val allPassed: Boolean
    get() = children.all { it.status is Passed }

  override fun toError(): Throwable? = when (status) {
    is Failed -> CompoundAssertionFailure(writeToString(), children.toErrors())
    is Pending -> TestSkippedException(writeToString())
    is Passed -> null
  }
}

private fun Iterable<ResultNode>.toErrors() =
  mapNotNull { it.toError() }
