package strikt.assertions

import strikt.api.Assertion

inline fun <reified E : Throwable> Assertion<() -> Unit>.throws(): Assertion<E> {
  var exception: E? = null
  assert("throws ${E::class.java.article} ${E::class.java.simpleName}") {
    val caught = try {
      subject()
      null
    } catch (e: Throwable) {
      e
    }
    when (caught) {
      null -> fail("no exception was caught") // TODO: is this rendering right?
      is E -> {
        pass()
        exception = caught
      }
      else -> fail(E::class.java, caught.javaClass)
    }
  }
  return if (exception != null) map { exception!! } else throw IllegalStateException()
}

val Class<*>.article: String
  get() = if (simpleName.contains("^[AEIOU]".toRegex())) "an" else "a"