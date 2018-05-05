package kirk.api

/**
 * The basic state of an assertion result.
 */
enum class Status {
  /**
   * The assertion passed.
   */
  Passed,
  /**
   * The assertion failed.
   */
  Failed
  // TODO: may want an `Error` too
}