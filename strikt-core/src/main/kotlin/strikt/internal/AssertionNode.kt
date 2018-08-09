package strikt.internal

import org.opentest4j.TestSkippedException
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
internal interface AssertionNode<S> {
  val subject: S
  var description: String
  val status: Status
  val root: AssertionNode<*>
  fun toError(): Throwable?
}

internal interface AssertionGroup<S> : AssertionNode<S> {
  fun append(node: AssertionNode<*>) // TODO: not visible outside of this hierarchy
  val children: Iterable<AssertionNode<*>>
}

internal interface AssertionResult<S> : AssertionNode<S> {
  val parent: AssertionGroup<S>
  val expected: Any?

  override fun toError(): Throwable? = when (status) {
    is Pending -> TestSkippedException(root.writeToString())
    is Passed -> null
    is Failed -> AtomicAssertionFailure(this)
  }
}

internal class AssertionSubject<S>(
  private val parent: AssertionGroup<*>?,
  override val subject: S,
  override var description: String = "%s"
) : AssertionGroup<S> {
  constructor(value: S) : this(null, value)

  override val root: AssertionNode<*>
    get() = parent?.root ?: this

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
      children.any { it.status is Failed } -> Failed()
      else -> Passed
    }

  override fun toError(): Throwable? =
    children
      .mapNotNull(AssertionNode<*>::toError)
      .let { errors ->
        when (errors.size) {
          0 -> null
          else -> CompoundAssertionFailure(root.writeToString(), errors)
        }
      }
}

internal abstract class AtomicAssertionNode<S>(
  final override val parent: AssertionGroup<S>,
  override var description: String,
  override val expected: Any? = null
) : AssertionResult<S> {

  override val subject: S
    get() = parent.subject

  override val root: AssertionNode<*>
    get() = parent.root ?: this

  init {
    parent.also { it.append(this) }
  }
}

internal abstract class CompoundAssertionNode<S>(
  final override val parent: AssertionGroup<S>,
  override var description: String,
  override val expected: Any? = null
) : AssertionGroup<S>, AssertionResult<S> {

  override val subject: S
    get() = parent.subject

  override val root: AssertionNode<*>
    get() = parent.root ?: this

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
