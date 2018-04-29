package assertions

interface Assertion<T> {
  fun evaluate(predicate: (T) -> Result)
}

internal class FailFastAssertion<T>(private val target: T) : Assertion<T> {
  override fun evaluate(predicate: (T) -> Result) {
    predicate(target).let { result ->
      when (result) {
        is Success<*> -> "Assertion passed for ${result.actual}:\n ✔ ${result.description}".let { message ->
          println(message)
        }
        is Failure<*> -> "Assertion failed for ${result.actual}:\n ✘ ${result.description}".let { message ->
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

sealed class Result

data class Success<T>(val actual: T, val description: String) : Result()

data class Failure<T>(val actual: T, val description: String) : Result()

// TODO: may want to consider using this and catching any unexpected exceptions
// data class Error(val exception: Throwable) : Result<Nothing>()