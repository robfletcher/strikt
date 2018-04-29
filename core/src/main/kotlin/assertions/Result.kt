package assertions

import java.io.Writer

interface Result {
  fun describeTo(sink: Writer, indent: Int)
  fun describeTo(sink: Writer) {
    describeTo(sink, 0)
  }
}

interface Success : Result
interface Failure : Result

interface ResultWithActual<T> : Result {
  val actual: T
}

interface AtomicResult<T> : ResultWithActual<T> {
  val description: String
}

interface CompoundResult<T> : ResultWithActual<T> {
  val results: Iterable<Result>
}

data class AtomicSuccess<T>(override val actual: T, override val description: String) : AtomicResult<T>, Success {
  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✔ $actual $description".padStart(indent))
  }
}

data class AtomicFailure<T>(override val actual: T, override val description: String) : AtomicResult<T>, Failure {
  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✘ $actual $description".padStart(indent))
  }
}

data class CompoundSuccess<T>(override val actual: T, override val results: Iterable<Result>) : CompoundResult<T>, Success {
  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✔ $actual".padStart(indent))
    results.forEach {
      sink.append("\n")
      it.describeTo(sink, indent + 2)
    }
  }
}

data class CompoundFailure<T>(override val actual: T, override val results: Iterable<Result>) : CompoundResult<T>, Failure {
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
