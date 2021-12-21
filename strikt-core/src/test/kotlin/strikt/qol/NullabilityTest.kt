package strikt.qol

import failgood.Test
import failgood.describe
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.isNotNull

@Test
class NullabilityTest {
  val context = describe("nullability") {
    it("isNotNull is available and always true for non nullable receivers") {
      expectThat("definately not null string").isNotNull().contains("not null")
    }
  }
}
