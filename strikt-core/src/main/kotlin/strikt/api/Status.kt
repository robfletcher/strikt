package strikt.api

import strikt.internal.ComparedValues

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
  data class Passed(
    val description: String? = null,
    val comparison: ComparedValues? = null
  ) : Status()

  data class Failed(
    val description: String? = null,
    val comparison: ComparedValues? = null,
    val cause: Throwable? = null
  ) : Status()
}
