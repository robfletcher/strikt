package strikt.internal

import strikt.api.Asserter
import strikt.api.AssertionComposer
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.Status
import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending

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
}

internal class AssertionSubject<T>(
  parent: ResultNode?,
  val subject: Described<T>
) : ResultNode(parent) {
  constructor(value: T) : this(null, Described(value))

  override val status: Status
    get() = when {
      children.isEmpty() -> Pending
      children.any { it.status is Status.Pending } -> Pending
      children.any { it.status is Status.Failed } -> Failed()
      else -> Passed
    }
}

internal abstract class AssertionResult<T>(
  parent: ResultNode
) : ResultNode(parent) {
  abstract val description: String
  abstract val expected: Any?
}

internal class AtomicAssertionResult<T>(
  parent: ResultNode,
  override val subject: T,
  override val description: String,
  override val expected: Any? = null
) : AssertionResult<T>(parent), AtomicAssertion<T> {

  constructor(parent: AssertionSubject<T>, description: String, expected: Any?) :
    this(parent, parent.subject.value, description, expected)

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

internal class CompoundAssertionResult<T>(
  parent: ResultNode,
  override val subject: T,
  override val description: String,
  override val expected: Any? = null
) : AssertionResult<T>(parent), CompoundAssertion<T>, AssertionComposer<T> {

  constructor(parent: AssertionSubject<T>, description: String, expected: Any?) :
    this(parent, parent.subject.value, description, expected)

  override fun <E> expect(subject: E): Asserter<E> {
    TODO("not implemented")
  }

  override fun <E> expect(
    subject: E,
    block: Asserter<E>.() -> Unit
  ): Asserter<E> {
    TODO("not implemented")
  }

  override fun assert(
    description: String,
    assertion: AtomicAssertion<T>.() -> Unit
  ): Asserter<T> {
    TODO("not implemented")
  }

  override fun assert(
    description: String,
    expected: Any?,
    assertion: AtomicAssertion<T>.() -> Unit
  ): Asserter<T> {
    TODO("not implemented")
  }

  override val status: Status
    get() = TODO("not implemented")

  override fun pass() {
    TODO("not implemented")
  }

  override fun fail(actual: Any?, description: String?, cause: Throwable?) {
    TODO("not implemented")
  }

  override val anyFailed: Boolean
    get() = TODO("not implemented")
  override val allFailed: Boolean
    get() = TODO("not implemented")
  override val anyPassed: Boolean
    get() = TODO("not implemented")
  override val allPassed: Boolean
    get() = TODO("not implemented")
}
