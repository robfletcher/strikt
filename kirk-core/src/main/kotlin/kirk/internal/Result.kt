package kirk.internal

import kirk.api.Result
import kirk.api.Status
import java.io.Writer

internal fun <T> result(status: Status, description: String, actual: T): Result =
  AtomicResult(status, description, actual)

internal fun <T> result(status: Status, description: String, actual: T, results: Iterable<Result>): Result =
  CompoundResult(status, description, actual, results)

private data class AtomicResult(
  override val status: Status,
  override val description: String,
  override val subject: Any?
) : Result {
  override fun describeTo(writer: Writer, indent: Int) {
    writer.append("".padStart(indent))
    writer.append(when (status) {
      Status.Passed -> "✔ "
      Status.Failed -> "✘ "
    })
    writer.write("$subject $description")
  }

  override val assertionCount = 1
  override val passCount = when (status) {
    Status.Passed -> 1
    Status.Failed -> 0
  }
  override val failureCount = when (status) {
    Status.Passed -> 0
    Status.Failed -> 1
  }
}

private data class CompoundResult(
  override val status: Status,
  override val description: String,
  override val subject: Any?,
  val results: Iterable<Result>
) : Result {
  override fun describeTo(writer: Writer, indent: Int) {
    writer.append("".padStart(indent))
    writer.append(when (status) {
      Status.Passed -> "✔ "
      Status.Failed -> "✘ "
    })
    writer.append("$subject $description")
    writer.append(":")
    results.forEach {
      writer.append("\n")
      it.describeTo(writer, indent + 2)
    }
  }

  override val assertionCount = results.sumBy { it.assertionCount }
  override val passCount = results.sumBy { it.passCount }
  override val failureCount = results.sumBy { it.failureCount }
}

internal fun Iterable<Result>.describeTo(writer: Writer) {
  firstOrNull()?.describeTo(writer)
  drop(1).forEach {
    writer.append('\n')
    it.describeTo(writer)
  }
}