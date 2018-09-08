package strikt.internal

import org.opentest4j.TestSkippedException
import strikt.api.Status
import strikt.api.Status.AssertionFailed
import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.opentest4j.AtomicAssertionFailure
import strikt.internal.reporting.writeToString

/**
 * Part of a graph of assertion results.
 *
 * @property status The status of the assertion or group of assertions.
 * @property root The root of the assertion graph.
 */
internal interface AssertionNode<S> {
  val subject: S
  var description: String
  val status: Status
  val root: AssertionSubject<*>
  fun toErrors(): List<Throwable>
  val parent: AssertionGroup<*>?
}

internal interface AssertionGroup<S> : AssertionNode<S> {
  fun append(node: AssertionNode<*>) // TODO: not visible outside of this hierarchy
  val children: Iterable<AssertionNode<*>>

  override fun toErrors(): List<Throwable> =
    children.flatMap(AssertionNode<*>::toErrors)
}

internal interface AssertionResult<S> : AssertionNode<S> {
  override val parent: AssertionGroup<S>
  val expected: Any?
}

internal class AssertionSubject<S>(
  override val parent: AssertionGroup<*>?,
  override val subject: S,
  override var description: String = "%s"
) : AssertionGroup<S> {
  constructor(value: S) : this(null, value)

  override val root: AssertionSubject<*>
    get() = parent?.root ?: this

  val isRoot = root === this

  init {
    parent?.also { it.append(this) }
  }

  private val _children = mutableListOf<AssertionNode<*>>()
  override val children: List<AssertionNode<*>>
    get() = _children

  override fun append(node: AssertionNode<*>) {
    _children.add(node)
  }

  override val status: Status
    get() = when {
      children.isEmpty() -> Pending
      children.any { it.status is Pending } -> Pending
      children.any { it.status is Failed } -> AssertionFailed()
      else -> Passed
    }
}

internal abstract class AtomicAssertionNode<S>(
  final override val parent: AssertionGroup<S>,
  override var description: String,
  override val expected: Any? = null
) : AssertionResult<S> {

  override val subject: S
    get() = parent.subject

  override val root: AssertionSubject<*>
    get() = parent.root

  init {
    parent.also { it.append(this) }
  }

  override fun toErrors(): List<Throwable> = when (status) {
    is Pending -> listOf(TestSkippedException(root.writeToString()))
    is Passed -> emptyList()
    is Failed -> listOf(AtomicAssertionFailure(this))
  }
}

internal abstract class CompoundAssertionNode<S>(
  final override val parent: AssertionGroup<S>,
  override var description: String,
  override val expected: Any? = null
) : AssertionGroup<S>, AssertionResult<S> {

  override val subject: S
    get() = parent.subject

  override val root: AssertionSubject<*>
    get() = parent.root

  init {
    parent.also { it.append(this) }
  }

  private val _children = mutableListOf<AssertionNode<*>>()
  override val children: List<AssertionNode<*>>
    get() = _children

  override fun append(node: AssertionNode<*>) {
    _children.add(node)
  }
}
