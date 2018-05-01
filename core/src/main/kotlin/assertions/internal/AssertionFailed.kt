package assertions.internal

import java.io.StringWriter

internal class AssertionFailed(val results: Collection<Result>) : AssertionError(
  StringWriter().also { writer -> results.describeTo(writer) }.toString()
) {
  constructor(result: Result) : this(listOf(result))

  val assertionCount = results.sumBy { it.assertionCount }
  val passCount = results.sumBy { it.passCount }
  val failureCount = results.sumBy { it.failureCount }
}