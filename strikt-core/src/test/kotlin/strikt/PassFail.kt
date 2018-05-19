package strikt

import org.junit.jupiter.api.assertThrows
import org.opentest4j.MultipleFailuresError

internal fun fails(function: () -> Unit) =
  assertThrows<MultipleFailuresError>(function)