package strikt.api

import strikt.api.Assertion.Builder

interface DescribeableBuilder<T> : Builder<T> {
  /**
   * Adds a description to the assertion.
   *
   * @param description a description of the subject of the assertion. The
   * description may include a [String.format] style placeholder for the value
   * itself.
   * @return the same assertion with the new description applied.
   */
  fun describedAs(description: String): Builder<T>
}
