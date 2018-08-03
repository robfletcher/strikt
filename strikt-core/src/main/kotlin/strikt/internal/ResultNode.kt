package strikt.internal

import strikt.api.Status

/**
 * Part of a graph of assertion results.
 *
 * @property status The status of the assertion or group of assertions.
 * @property root The root of the assertion graph.
 */
// TODO: name; this is not always a result, it can be a container, e.g. map() Probably change to AssertionContext
// TODO: should this be a sealed class? It's used in when blocks everywhere
internal interface ResultNode {
  val parent: ResultNode?
  val status: Status

  val root: ResultNode
    get() = parent?.root ?: this

  val children: List<ResultNode>
}
