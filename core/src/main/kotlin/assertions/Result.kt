package assertions

import java.io.Writer

interface Result {
  val description: String
  val assertionCount: Int
  val passCount: Int
  val failureCount: Int

  fun describeTo(writer: Writer, indent: Int)
  fun describeTo(writer: Writer) {
    describeTo(writer, 0)
  }

  companion object {
    fun <T> success(actual: T, description: String): Success =
      AtomicSuccess(actual, description)

    fun <T> failure(actual: T, description: String): Failure =
      AtomicFailure(actual, description)

    fun <T> success(actual: T, description: String, results: Iterable<Result>): Success =
      CompoundSuccess(actual, description, results)

    fun <T> failure(actual: T, description: String, results: Iterable<Result>): Failure =
      CompoundFailure(actual, description, results)
  }
}

interface Success : Result
interface Failure : Result

private interface AtomicResult<T> : Result {
  val actual: T
}

private interface CompoundResult<T> : AtomicResult<T> {
  val results: Iterable<Result>
}

private data class AtomicSuccess<T>(override val actual: T, override val description: String) : AtomicResult<T>, Success {
  override fun describeTo(writer: Writer, indent: Int) {
    (0 until indent).forEach { writer.append(' ') }
    writer.write("✔ $actual $description".padStart(indent))
  }

  override val assertionCount = 1
  override val passCount = 1
  override val failureCount = 0
}

private data class AtomicFailure<T>(override val actual: T, override val description: String) : AtomicResult<T>, Failure {
  override fun describeTo(writer: Writer, indent: Int) {
    (0 until indent).forEach { writer.append(' ') }
    writer.write("✘ $actual $description".padStart(indent))
  }

  override val assertionCount = 1
  override val passCount = 0
  override val failureCount = 1
}

private data class CompoundSuccess<T>(override val actual: T, override val description: String, override val results: Iterable<Result>) : CompoundResult<T>, Success {
  override fun describeTo(writer: Writer, indent: Int) {
    (0 until indent).forEach { writer.append(' ') }
    writer.write("✔ $actual $description: ".padStart(indent))
    results.forEach {
      writer.append("\n")
      it.describeTo(writer, indent + 2)
    }
  }

  override val assertionCount = results.sumBy { it.assertionCount }
  override val passCount = results.sumBy { it.passCount }
  override val failureCount = results.sumBy { it.failureCount }
}

private data class CompoundFailure<T>(override val actual: T, override val description: String, override val results: Iterable<Result>) : CompoundResult<T>, Failure {
  override fun describeTo(writer: Writer, indent: Int) {
    (0 until indent).forEach { writer.append(' ') }
    writer.write("✘ $actual $description: ".padStart(indent))
    results.forEach {
      writer.append("\n")
      it.describeTo(writer, indent + 2)
    }
  }

  override val assertionCount = results.sumBy { it.assertionCount }
  override val passCount = results.sumBy { it.passCount }
  override val failureCount = results.sumBy { it.failureCount }
}

// TODO: may want to consider using this and catching any unexpected exceptions
// data class Error(val exception: Throwable) : Result<Nothing>()
