package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Asserts that the subject function throws an exception of type [E] when
 * executed.
 *
 * @return an assertion over the thrown exception, allowing further assertions
 * about messages, root causes, etc.
 */
inline fun <reified E : Throwable> Builder<() -> Unit>.throws(): Builder<E> {
  var exception: E? = null
  assert("throws %s", E::class.java) {
    val caught = try {
      it()
      null
    } catch (e: Throwable) {
      e
    }
    when (caught) {
      null -> fail(description = "nothing was thrown")
      is E -> {
        pass()
        exception = caught
      }
      else -> fail(
        actual = caught.javaClass,
        description = "%s was thrown",
        cause = caught
      )
    }
  }
  return if (exception != null) {
    map("thrown exception") { exception!! }
  } else {
    throw IllegalStateException()
  }
}
