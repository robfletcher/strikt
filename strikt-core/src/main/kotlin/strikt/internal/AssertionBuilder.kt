package strikt.internal

import strikt.api.Assertion.Builder
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.CompoundAssertions
import strikt.api.DescribeableBuilder
import strikt.internal.AssertionStrategy.Collecting
import strikt.internal.AssertionStrategy.Negating

internal class AssertionBuilder<T>(
  private val context: AssertionGroup<T>,
  private val strategy: AssertionStrategy
) : DescribeableBuilder<T> {

  override fun describedAs(description: String): Builder<T> {
    if (context is DescribedNode<*>) {
      context.description = description
    }
    return this
  }

  override fun describedAs(descriptor: T.() -> String): Builder<T> {
    if (context is DescribedNode<*>) {
      context.description = context.subject.descriptor()
    }
    return this
  }

  override fun and(
    assertions: Builder<T>.() -> Unit
  ): Builder<T> =
    AssertionChainedGroup(context, context.subject)
      .let { nestedContext ->
        // collect assertions from a child block
        AssertionBuilder(nestedContext, Collecting)
          .apply(assertions)
          .also {
            strategy.evaluate(nestedContext)
          }
        // return the original builder for chaining
        this
      }

  override fun not(assertions: Builder<T>.() -> Unit): Builder<T> {
    AssertionBuilder(context, Negating(Collecting)).apply(assertions)
    strategy.evaluate(context)
    return this
  }

  override fun assert(
    description: String,
    expected: Any?,
    assert: AtomicAssertion.(T) -> Unit
  ): AssertionBuilder<T> {
    val chain = if (context is AssertionChain<T>) {
      context
    } else {
      AssertionChain(context, context.subject)
    }
    if (chain.allowChain) {
      val assertion = strategy
        .appendAtomic(chain, description, expected)
      assertion.assert(chain.subject)
    }
    return AssertionBuilder(chain, strategy)
  }

  override fun compose(
    description: String,
    expected: Any?,
    assertions: Builder<T>.(T) -> Unit
  ): CompoundAssertions<T> {
    val composedContext = strategy
      .appendCompound(context, description, expected)
    AssertionBuilder(composedContext, Collecting).apply {
      assertions(context.subject)
    }
    return object : CompoundAssertions<T> {
      override fun then(block: CompoundAssertion.() -> Unit): Builder<T> {
        composedContext.block()
        return this@AssertionBuilder
      }
    }
  }

  override fun <R> get(
    description: String,
    function: (T) -> R
  ): DescribeableBuilder<R> =
    if (context.allowChain) {
      val mappedValue = function(context.subject)
      AssertionBuilder(
        AssertionSubject(context, mappedValue, description),
        strategy
      )
    } else {
      this as AssertionBuilder<R> // TODO: no, this is a lie
    }

  override fun not(): Builder<T> = AssertionBuilder(
    context,
    Negating(strategy)
  )
}
