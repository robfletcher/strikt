package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat

@DisplayName("assertions on enums")
internal class EnumAssertions {
  @TestFactory
  fun `can map to the enum name"`() {
    for (deity in Pantheon.values()) {
      dynamicTest("Can get name on $deity") {
        expectThat(deity).name.isEqualTo(deity.name)
      }
    }
  }

  @TestFactory
  fun `can map to the enum ordinal`() {
    for (deity in Pantheon.values()) {
      dynamicTest("Can get ordinal on $deity") {
        expectThat(deity).ordinal.isEqualTo(deity.ordinal)
      }
    }
  }
}

enum class Pantheon {
  NORSE, GREEK, ROMAN
}
