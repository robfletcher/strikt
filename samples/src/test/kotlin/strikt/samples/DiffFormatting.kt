package strikt.samples

import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.isEqualTo

internal object DiffFormatting {

  @Test
  fun formatsBlockDiffInIntelliJ() {
    expect("Expect that: %s", "o hai") {
      isEqualTo("kthxbye")
      isEqualTo("o HAi")
    }
  }

  @Test
  fun formatsChainDiffInIntelliJ() {
    expect("o hai").isEqualTo("o HAi")
  }
}