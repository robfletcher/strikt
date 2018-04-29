package assertions

import java.io.StringWriter
import java.io.Writer

interface Assertion<T> {
  fun evaluate(predicate: (T) -> Result)
}

internal class FailFastAssertion<T>(private val target: T) : Assertion<T> {
  override fun evaluate(predicate: (T) -> Result) {
    predicate(target).let { result ->
      val message = StringWriter()
        .also { writer -> result.describeTo(writer) }
        .toString()
      println(message)
      if (result is Failure) {
        throw AssertionError(message)
      }
    }
  }
}

interface Result {
  val success: Boolean
  fun describeTo(sink: Writer, indent: Int)
  fun describeTo(sink: Writer) {
    describeTo(sink, 0)
  }
}

interface Success : Result
interface Failure : Result

interface ResultWithActual<T> : Result {
  abstract val actual: T
}

interface AtomicResult<T> : ResultWithActual<T> {
  abstract val description: String
}

interface CompoundResult<T> : ResultWithActual<T> {
  abstract val results: Iterable<Result>
}

data class AtomicSuccess<T>(override val actual: T, override val description: String) : AtomicResult<T>, Success {
  override val success = true

  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✔ $actual $description".padStart(indent))
  }
}

data class AtomicFailure<T>(override val actual: T, override val description: String) : AtomicResult<T>, Failure {
  override val success = false

  override fun describeTo(sink: Writer, indent: Int) {
    (0 until indent).forEach { sink.append(' ') }
    sink.write("✘ $actual $description".padStart(indent))
  }
}

data class CompoundSuccess<T>(override val actual: T, override val results: Iterable<Result>) : CompoundResult<T>, Success {
  override val success = true

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
  override val success = false

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
