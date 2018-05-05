package kirk.internal

import kirk.api.Result
import kirk.internal.reporting.DefaultResultWriter
import java.io.StringWriter

internal class AssertionFailed(
  val results: Collection<Result>
) : AssertionError(
  StringWriter().use { writer ->
    DefaultResultWriter(writer).write(results)
    writer.toString()
  }
) {
  constructor(result: Result) : this(listOf(result))

  val assertionCount = results.sumBy { it.assertionCount }
  val passCount = results.sumBy { it.passCount }
  val failureCount = results.sumBy { it.failureCount }
}