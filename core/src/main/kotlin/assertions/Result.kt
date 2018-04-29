package assertions

import java.io.Writer

interface Result {
  fun describeTo(sink: Writer, indent: Int)
  fun describeTo(sink: Writer) {
    describeTo(sink, 0)
  }

  companion object {
    fun <T> success(actual: T, description: String): Success =
      AtomicSuccess(actual, description)

    fun <T> failure(actual: T, description: String): Failure =
      AtomicFailure(actual, description)

    fun <T> success(actual: T, results: Iterable<Result>): Success =
      CompoundSuccess(actual, results)

    fun <T> failure(actual: T, results: Iterable<Result>): Failure =
      CompoundFailure(actual, results)
  }
}

interface Success : Result
interface Failure : Result

private interface ResultWithActual<T> : Result {
  val actual: T
}

private interface AtomicResult<T> : ResultWithActual<T> {
  val description: String
}

private interface CompoundResult<T> : ResultWithActual<T> {
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

private data class CompoundSuccess<T>(override val actual: T, override val results: Iterable<Result>) : CompoundResult<T>, Success {
  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✔ $actual".padStart(indent))
    results.forEach {
      sink.append("\n")
      it.describeTo(sink, indent + 2)
    }
  }
}

private data class CompoundFailure<T>(override val actual: T, override val results: Iterable<Result>) : CompoundResult<T>, Failure {
  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✘ $actual".padStart(indent))
    results.forEach {
      sink.append("\n")
      it.describeTo(sink, indent + 2)
    }
  }
}

// TODO: may want to consider using this and catching any unexpected exceptions
// data class Error(val exception: Throwable) : Result<Nothing>()
