package strikt.internal

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
internal interface AssertionNode<S> {
  val subject: S
  val status: Status
  val root: AssertionNode<*>
  val parent: AssertionGroup<*>?
  val allowChain: Boolean
    get() = true
}

internal interface DescribedNode<S> : AssertionNode<S> {
  var description: String
}

internal interface AssertionGroup<S> : AssertionNode<S> {
  fun append(node: AssertionNode<*>) // TODO: not visible outside of this hierarchy

  val children: Iterable<AssertionNode<*>>
}

internal interface AssertionResult<S> : DescribedNode<S> {
  override val parent: AssertionGroup<S>
  val expected: Any?
}

internal class AssertionSubject<S>(
  override val parent: AssertionGroup<*>?,
  override val subject: S,
  override var description: String = "%s"
) : AssertionGroup<S>, DescribedNode<S> {
  constructor(value: S) : this(null, value)

  override val root: AssertionNode<*>
    get() = parent?.root ?: this

  val isRoot = root === this

  init {
    parent?.append(this)
  }

  private val _children = mutableListOf<AssertionNode<*>>()
  override val children: List<AssertionNode<*>> =
    _children

  override fun append(node: AssertionNode<*>) {
    _children.add(node)
  }

  override val status: Status
    get() =
      when {
        children.isEmpty() -> Pending
        children.any { it.status is Failed } -> Failed()
        children.any { it.status is Pending } -> Pending
        else -> Passed()
      }
}

/**
 * Models the start of a chain of assertions and contains all the assertions in
 * the chain as children.
 */
internal class AssertionChain<S>(
  override val parent: AssertionGroup<*>,
  override val subject: S
) : AssertionGroup<S> {
  override val root: AssertionNode<*>
    get() = parent.root

  init {
    parent.append(this)
  }

  private val _children = mutableListOf<AssertionNode<*>>()
  override val children: List<AssertionNode<*>> =
    _children

  override fun append(node: AssertionNode<*>) {
    _children.add(node)
  }

  override val status: Status
    get() =
      when {
        children.isEmpty() -> Pending
        children.any { it.status is Failed } -> Failed()
        children.any { it.status is Pending } -> Pending
        else -> Passed()
      }

  override val allowChain: Boolean
    get() = children.map { it.status }.all { it is Passed }
}

/**
 * Models a group appended to an assertion chain, for example with
 * [strikt.api.Assertion.Builder.and].
 */
internal class AssertionChainedGroup<S>(
  override val parent: AssertionGroup<*>,
  override val subject: S
) : AssertionGroup<S> {
  override val root: AssertionNode<*>
    get() = parent.root

  init {
    parent.append(this)
  }

  private val _children = mutableListOf<AssertionNode<*>>()
  override val children: List<AssertionNode<*>> =
    _children

  override fun append(node: AssertionNode<*>) {
    _children.add(node)
  }

  override val status: Status
    get() =
      when {
        children.isEmpty() -> Pending
        children.any { it.status is Failed } -> Failed()
        children.any { it.status is Pending } -> Pending
        else -> Passed()
      }
}

internal abstract class AtomicAssertionNode<S>(
  final override val parent: AssertionGroup<S>,
  override var description: String,
  override val expected: Any? = null
) : AssertionResult<S>, AtomicAssertion {
  override val subject: S
    get() = parent.subject

  override val root: AssertionNode<*>
    get() = parent.root

  init {
    @Suppress("LeakingThis")
    parent.append(this)
  }
}

internal abstract class CompoundAssertionNode<S>(
  final override val parent: AssertionGroup<S>,
  override var description: String,
  override val expected: Any? = null
) : AssertionGroup<S>, AssertionResult<S>, CompoundAssertion {
  override val subject: S
    get() = parent.subject

  override val root: AssertionNode<*>
    get() = parent.root

  init {
    @Suppress("LeakingThis")
    parent.append(this)
  }

  private val _children = mutableListOf<AssertionNode<*>>()
  override val children: List<AssertionNode<*>> =
    _children

  override fun append(node: AssertionNode<*>) {
    _children.add(node)
  }
}
