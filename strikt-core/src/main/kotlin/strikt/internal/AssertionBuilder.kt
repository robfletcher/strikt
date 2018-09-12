package strikt.internal

import strikt.api.Assertion
import strikt.api.Assertion.Builder
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.CompoundAssertions
import strikt.api.DescribeableBuilder
import strikt.api.Status.Failed
import strikt.internal.opentest4j.AtomicAssertionFailure
import strikt.internal.opentest4j.CompoundAssertionFailure
import strikt.internal.reporting.writePartialToString
import strikt.internal.reporting.writeToString

internal class AssertionBuilder<T>(
  private val context: AssertionGroup<T>,
  private val strategy: AssertionStrategy
) : DescribeableBuilder<T> {

  override fun describedAs(description: String): Builder<T> {
    context.description = description
    return this
  }

  override fun and(
    assertions: Assertion.Builder<T>.() -> Unit
  ): Assertion.Builder<T> {
    AssertionBuilder(context, AssertionStrategy.Collecting)
      .apply(assertions)
    // TODO: this is a shitty hack and duplicates the logic in Expect.kt. Need a method on the strategy to re-evaluate collected assertions
    if (strategy is AssertionStrategy.Throwing) {
      if (context.children.any { it.status is Failed }) {
        throw CompoundAssertionFailure(
          context.root.writeToString(),
          context
            .children
            .filter { it.status is Failed }
            .map { AtomicAssertionFailure(it.writePartialToString(), it) }
        )
      }
    }
    return this
  }

  override fun assert(
    description: String,
    expected: Any?,
    assert: AtomicAssertion.(T) -> Unit
  ): AssertionBuilder<T> {
    val assertion = strategy.appendAtomic(
      context,
      description,
      expected
    )
    assertion.assert(context.subject)
    return this
  }

  override fun compose(
    description: String,
    expected: Any?,
    assertions: Builder<T>.(T) -> Unit
  ): CompoundAssertions<T> {
    val composedContext =
      strategy.appendCompound(context, description, expected)
    AssertionBuilder(composedContext, AssertionStrategy.Collecting).apply {
      assertions(context.subject)
    }
    return object : CompoundAssertions<T> {
      override fun then(block: CompoundAssertion.() -> Unit): Builder<T> {
        composedContext.block()
        return this@AssertionBuilder
      }
    }
  }

  override fun <R> map(
    description: String,
    function: (T) -> R
  ): DescribeableBuilder<R> =
    function(context.subject)
      .let { mappedValue ->
        AssertionBuilder(
          AssertionSubject(context, mappedValue, description),
          strategy
        )
      }

  override fun not(): Builder<T> = AssertionBuilder(
    context,
    AssertionStrategy.Negating(strategy)
  )
}
