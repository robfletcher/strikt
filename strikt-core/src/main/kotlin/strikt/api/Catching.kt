package strikt.api

/**
 * Executes [action], catching and returning any exception that is thrown. If
 * no exception is thrown the method returns `null`.
 *
 * @return the exception thrown by [action] or `null` if no exception is thrown.
 */
fun catching(
  action: () -> Unit
): Throwable? {
  return try {
    action()
    null
  } catch (actual: Throwable) {
    actual
  }
}
