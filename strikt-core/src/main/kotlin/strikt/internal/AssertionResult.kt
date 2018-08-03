package strikt.internal

import strikt.api.Assertion

/**
 * Used internally to gather results without exposing [ResultNode] interface to
 * assertion function implementors.
 */
internal abstract class AssertionResult<T>(
  override val parent: ResultNode?
) : Assertion<T>, ResultNode
