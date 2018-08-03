package strikt.internal

import strikt.api.AtomicAssertion
import strikt.api.Status

internal class AtomicAssertionResult<T>(
  override val subject: T,
  parent: ResultNode?
) : AssertionResult<T>(parent), AtomicAssertion<T> {
  private var _status: Status = Status.Pending

  override val status: Status
    get() = _status

  override val children: List<ResultNode>
    get() = emptyList()

  override fun pass() {
    _status = Status.Passed
  }

  override fun fail(actual: Any?, description: String?, cause: Throwable?) {
    _status = Status.Failed(actual, description, cause)
  }
}
