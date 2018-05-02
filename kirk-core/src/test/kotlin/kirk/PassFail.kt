package kirk

import kirk.internal.AssertionFailed

internal fun passes(function: () -> Unit) {
  function.invoke()
}

internal fun fails(function: () -> Unit): AssertionFailed {
  try {
    function.invoke()
    assert(false) { "Should have failed" }
    throw IllegalStateException("Unreachable statement unless assertions are not enabled")
  } catch (e: AssertionFailed) {
    return e
  }
}