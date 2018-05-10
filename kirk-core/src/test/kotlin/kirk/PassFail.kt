package kirk

import kirk.api.AssertionFailed
import org.junit.jupiter.api.assertThrows

internal fun fails(function: () -> Unit) =
  assertThrows<AssertionFailed>(function)