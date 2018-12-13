package strikt.assertions

import com.oneeyedmen.minutest.junit.testFactoryFor
import com.oneeyedmen.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat

@DisplayName("assertions on enums")
internal object EnumAssertions {
  @TestFactory
  fun `can map to the enum name"`() = testFactoryFor(rootContext<Unit> {
    for (deity in Pantheon.values()) {
      test("Can get name on $deity") {
        expectThat(deity).name.isEqualTo(deity.name)
      }
    }
  })

  @TestFactory
  fun `can map to the enum ordinal`() = testFactoryFor(rootContext<Unit> {
    for (deity in Pantheon.values()) {
      test("Can get ordinal on $deity") {
        expectThat(deity).ordinal.isEqualTo(deity.ordinal)
      }
    }
  })
}

enum class Pantheon(val ruler: String, val underworldRuler: String) {
  NORSE("Odin", "Hel"),
  GREEK("Zeus", "Hades"),
  ROMAN("Jupiter", "Pluto")
}

enum class Deity(val realm: Pantheon) {
  Eris(Pantheon.GREEK)
}
