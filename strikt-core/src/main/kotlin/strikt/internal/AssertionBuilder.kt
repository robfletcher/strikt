package strikt.internal

import strikt.api.Assertion
import strikt.api.Assertion.Builder
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.CompoundAssertions
import strikt.api.DescribeableBuilder

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
    AssertionBuilder(context, AssertionStrategy.Collecting).apply(assertions)
    strategy.evaluate(context)
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

  override fun <R> chain(
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
