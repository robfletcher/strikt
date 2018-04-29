package assertions

interface Assertion<T> {
  fun evaluate(predicate: (T) -> Result)
}

internal class FailFastAssertion<T>(private val target: T) : Assertion<T> {
  override fun evaluate(predicate: (T) -> Result) {
    predicate(target).apply {
      if (this is Success) {
        println("Assertion passed for $target")
      } else if (this is Failure<*>) {
        "Assertion failed for $target:\n ✖ $description".let { message ->
          println(message)
          throw AssertionError(message)
        }
      }
    }
  }
}

sealed class Result

object Success : Result()

data class Failure<T>(val actual: T, val description: String) : Result()

// TODO: may want to consider using this and catching any unexpected exceptions
// data class Error(val exception: Throwable) : Result<Nothing>()