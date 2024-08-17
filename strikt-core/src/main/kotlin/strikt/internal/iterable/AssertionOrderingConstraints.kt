package strikt.internal.iterable

import strikt.assertions.OrderingConstraintsAssertScope
import strikt.assertions.OrderingConstraintsBuilder
import strikt.internal.iterable.SectionAssertionSpec.EndDefinition

internal class OrderingConstraintsAssertScopeImpl<E>: OrderingConstraintsAssertScope<E> {

  val sections = mutableListOf<SectionAssertionSpec<E>>()
  var currentBuildingSection = SectionAssertionSpec<E>()
  var expectsNoFurtherElements = false

  override fun expect(element: E): OrderingConstraintsBuilder<E> {
    require(!expectsNoFurtherElements) { "expectNoFurtherElements was previously called" }
    require(currentBuildingSection.elementsWithConstraints.none { it.element == element }) {
      "Element $element already expected in this section!\n" +
        "Use startNewSection if this element is expected to be in the list multiple times. " +
        "Ordering constraints are asserted within each section."
    }
    val constraintsBuilder = OrderingConstraintsBuilderImpl(element, currentBuildingSection)
    currentBuildingSection.elementsWithConstraints +=
      ElementWithOrderingConstraints(element, constraintsBuilder.constraints)
    return constraintsBuilder
  }

  override fun expectNoOtherElements() {
    currentBuildingSection.expectsNoOtherElements = true
  }

  override fun expectNoFurtherElements() {
    expectsNoFurtherElements = true
    currentBuildingSection.expectsNoOtherElements = true
  }

  override fun startNewSection() = endSectionIfInProgress()

  fun endSectionIfInProgress() {
    if (currentBuildingSection.elementsWithConstraints.isEmpty()) return
    sections += currentBuildingSection
    currentBuildingSection = SectionAssertionSpec()
  }

  internal class OrderingConstraintsBuilderImpl<E>(
    private val element: E,
    private val section: SectionAssertionSpec<E>,
  ) : OrderingConstraintsBuilder<E> {

    val constraints = mutableListOf<OrderingConstraint<E>>()

    override fun first(): OrderingConstraintsBuilderImpl<E> {
      constraints += OrderingConstraint.First
      return this
    }

    override fun last(): OrderingConstraintsBuilderImpl<E> {
      constraints += OrderingConstraint.Last
      section.endDefinedBy = EndDefinition.DeclaredElement(element)
      return this
    }

    override fun before(other: E): OrderingConstraintsBuilderImpl<E> {
      constraints += OrderingConstraint.Before(other)
      return this
    }

    override fun immediatelyBefore(other: E): OrderingConstraintsBuilder<E> {
      constraints += OrderingConstraint.ImmediatelyBefore(other)
      return this
    }

    override fun after(other: E): OrderingConstraintsBuilderImpl<E> {
      constraints += OrderingConstraint.After(other)
      return this
    }

    override fun immediatelyAfter(other: E): OrderingConstraintsBuilder<E> {
      constraints += OrderingConstraint.ImmediatelyAfter(other)
      return this
    }

    override fun immediatelyAfterPrevious(): OrderingConstraintsBuilderImpl<E> {
      require(section.elementsWithConstraints.isNotEmpty()) {
        "immediatelyAfterPrevious requires declaring an expected element before this one"
      }
      constraints += OrderingConstraint.ImmediatelyAfterPrevious
      return this
    }
  }
}

internal data class ElementWithOrderingConstraints<E>(val element: E, val constraints: List<OrderingConstraint<E>>)

internal class SectionAssertionSpec<E> {
  val elementsWithConstraints = mutableListOf<ElementWithOrderingConstraints<E>>()
  var endDefinedBy: EndDefinition<E> = EndDefinition.Undefined
  var expectsNoOtherElements = false

  sealed class EndDefinition<out E> {
    data object Undefined : EndDefinition<Nothing>()
    data class DeclaredElement<E>(val element: E) : EndDefinition<E>()
  }
}

internal sealed class OrderingConstraint<out E> {
  data object First: OrderingConstraint<Nothing>()
  data object Last: OrderingConstraint<Nothing>()
  data class Before<E>(val target: E) : OrderingConstraint<E>()
  data class ImmediatelyBefore<E>(val target: E) : OrderingConstraint<E>()
  data class After<E>(val target: E) : OrderingConstraint<E>()
  data class ImmediatelyAfter<E>(val target: E) : OrderingConstraint<E>()
  data object ImmediatelyAfterPrevious : OrderingConstraint<Nothing>()
}
