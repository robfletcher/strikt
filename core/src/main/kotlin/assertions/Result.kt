package assertions

import java.io.Writer

sealed class Status {
  object Success : Status()
  object Failure : Status()
  // TODO: may want an `Error` too
}

interface Result {
  val status: Status
  val description: String
  val actual: Any?
  val assertionCount: Int
  val passCount: Int
  val failureCount: Int

  fun describeTo(writer: Writer, indent: Int)

  fun describeTo(writer: Writer) {
    describeTo(writer, 0)
  }
}

internal fun <T> result(status: Status, description: String, actual: T): Result =
  AtomicResult(status, description, actual)

internal fun <T> result(status: Status, description: String, actual: T, results: Iterable<Result>): Result =
  CompoundResult(status, description, actual, results)

private data class AtomicResult(
  override val status: Status,
  override val description: String,
  override val actual: Any?
) : Result {
  override fun describeTo(writer: Writer, indent: Int) {
    writer.append("".padStart(indent))
    writer.append(when (status) {
      Status.Success -> "✔ "
      Status.Failure -> "✘ "
    })
    writer.write("$actual $description")
  }

  override val assertionCount = 1
  override val passCount = when (status) {
    Status.Success -> 1
    Status.Failure -> 0
  }
  override val failureCount = when (status) {
    Status.Success -> 0
    Status.Failure -> 1
  }
}

private data class CompoundResult(
  override val status: Status,
  override val description: String,
  override val actual: Any?,
  val results: Iterable<Result>
) : Result {
  override fun describeTo(writer: Writer, indent: Int) {
    writer.append("".padStart(indent))
    writer.append(when (status) {
      Status.Success -> "✔ "
      Status.Failure -> "✘ "
    })
    writer.append("$actual $description")
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