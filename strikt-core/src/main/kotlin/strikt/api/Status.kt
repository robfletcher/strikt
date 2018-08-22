package strikt.api

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

  abstract class Failed : Status() {
    abstract fun describe(formatter: (Any?) -> Any?): String?
    abstract val cause: Throwable?
  }

  /**
   * The assertion failed.
   */
  data class AssertionFailed(
    val description: String? = null,
    override val cause: Throwable? = null
  ) : Failed() {
    override fun describe(formatter: (Any?) -> Any?): String? =
      description
  }

  data class ComparisonFailed(
    val expected: Any?,
    val actual: Any?,
    val description: String? = null,
    override val cause: Throwable? = null
  ) : Failed() {
    override fun describe(formatter: (Any?) -> Any?): String? =
      when (actual) {
        null -> description
        else -> description?.format(formatter(actual))
      }
  }
}
