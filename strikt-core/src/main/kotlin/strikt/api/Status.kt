package strikt.api

import strikt.internal.ValueComparison

/**
 * The basic state of an assertion result.
 */
internal sealed class Status {
  /**
   * The assertion was not evaluated or has not been evaluated yet.
   */
  object Pending : Status()

  /**
   * The assertion passed.
   */
  object Passed : Status()

  data class Failed(
    val description: String? = null,
    val comparison: ValueComparison<Any?>? = null, // TODO: declare a type
    val cause: Throwable? = null
  ) : Status() {
    fun describe(formatter: (Any?) -> Any?): String? =
      when (comparison) {
        null -> description
        else -> description?.format(formatter(comparison.actual))
      }
  }
}
