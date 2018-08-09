package strikt

import org.junit.jupiter.api.assertThrows

internal fun fails(function: () -> Unit) =
  assertThrows<AssertionError>(function)
