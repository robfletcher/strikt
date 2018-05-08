package kirk

import kirk.api.AssertionFailed
import kotlin.test.fail

internal fun fails(function: () -> Unit): AssertionFailed {
  try {
    function.invoke()
    fail("Should have failed with ${AssertionFailed::class.java.name} but no exception was thrown")
  } catch (e: AssertionFailed) {
    return e
  } catch (e: Throwable) {
    fail("Should have failed with ${AssertionFailed::class.java.name} but caught a ${e.javaClass.name} instead")
  }
}