package assertions

import java.io.Writer

interface Result {
  val description: String

  fun describeTo(sink: Writer, indent: Int)
  fun describeTo(sink: Writer) {
    describeTo(sink, 0)
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
  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✔ $actual $description".padStart(indent))
  }
}

private data class AtomicFailure<T>(override val actual: T, override val description: String) : AtomicResult<T>, Failure {
  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✘ $actual $description".padStart(indent))
  }
}

private data class CompoundSuccess<T>(override val actual: T, override val description: String, override val results: Iterable<Result>) : CompoundResult<T>, Success {
  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✔ $actual $description".padStart(indent))
    results.forEach {
      sink.append("\n")
      it.describeTo(sink, indent + 2)
    }
  }
}

private data class CompoundFailure<T>(override val actual: T, override val description: String, override val results: Iterable<Result>) : CompoundResult<T>, Failure {
  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✘ $actual $description".padStart(indent))
    results.forEach {
      sink.append("\n")
      it.describeTo(sink, indent + 2)
    }
  }
}

// TODO: may want to consider using this and catching any unexpected exceptions
// data class Error(val exception: Throwable) : Result<Nothing>()
