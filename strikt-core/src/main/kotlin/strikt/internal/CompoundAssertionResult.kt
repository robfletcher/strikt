package strikt.internal

import strikt.api.Asserter
import strikt.api.AssertionComposer
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.Status

internal class CompoundAssertionResult<T>(
  override val subject: T,
  parent: ResultNode?
) : AssertionResult<T>(parent), CompoundAssertion<T>, AssertionComposer<T> {
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

  override val children: List<ResultNode>
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
