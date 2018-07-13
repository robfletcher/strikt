package strikt.assertions

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import strikt.api.expect

internal object EnumAssertions : Spek({
  describe("assertions on enums") {
    Pantheon.values().forEach { pantheon ->
      it("can map to the enum name \"${pantheon.name}\"") {
        expect(pantheon).name.isEqualTo(pantheon.name)
      }

      it("can map to the enum ordinal ${pantheon.ordinal}") {
        expect(pantheon).ordinal.isEqualTo(pantheon.ordinal)
      }
    }
  }
})

enum class Pantheon {
  NORSE, GREEK, ROMAN
}
