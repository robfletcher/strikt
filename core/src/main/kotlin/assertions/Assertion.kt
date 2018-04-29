package assertions

interface Assertion<T> {
  fun evaluate(predicate: (T) -> Result)
}

internal class FailFastAssertion<T>(private val target: T) : Assertion<T> {
  override fun evaluate(predicate: (T) -> Result) {
    predicate(target).let { result ->
      when (result) {
        is Success<*> -> "✔ ${result.actual}:\n${result.descriptions.join()}".let { message ->
          println(message)
        }
        is Failure<*> -> "✘ ${result.actual}:\n${result.descriptions.join()}".let { message ->
          println(message)
          throw AssertionError(message)
        }
      }
    }
  }
}

internal class GroupingAssertion<T>(private val target: T) : Assertion<T> {
  override fun evaluate(predicate: (T) -> Result) {
    TODO("not implemented")
  }
}

sealed class Result {
  abstract val descriptions: List<Description>
}

data class Success<T>(val actual: T, override val descriptions: List<Description>) : Result() {
  constructor(actual: T, description: String) : this(actual, listOf(Pass(description)))
}

data class Failure<T>(val actual: T, override val descriptions: List<Description>) : Result() {
  constructor(actual: T, description: String) : this(actual, listOf(Fail(description)))
}

// TODO: may want to consider using this and catching any unexpected exceptions
// data class Error(val exception: Throwable) : Result<Nothing>()

sealed class Description() {
  abstract val message: String
}

data class Pass(override val message: String) : Description() {
  override fun toString(): String {
    return "✔ $message"
  }
}

data class Fail(override val message: String) : Description() {
  override fun toString(): String {
    return "✘ $message"
  }
}

private fun Iterable<Description>.join() =
  joinToString("\n") { it.toString() }