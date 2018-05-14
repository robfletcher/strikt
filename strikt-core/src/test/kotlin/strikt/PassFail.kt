package strikt

import org.junit.jupiter.api.assertThrows
import strikt.api.AssertionFailed

internal fun fails(function: () -> Unit) =
  assertThrows<AssertionFailed>(function)