package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
import strikt.api.expectThrows

@DisplayName("assertions on enums")
internal object EnumAssertions : JUnit5Minutests {
  fun tests() =
    rootContext {
      context("name mapping") {
        Pantheon.values().forEach { deity ->
          test("Can get name on $deity") {
            expectThat(deity).name.isEqualTo(deity.name)
          }
        }
      }

      context("ordinal mapping") {
        Pantheon.values().forEach { deity ->
          test("Can get ordinal on $deity") {
            expectThat(deity).ordinal.isEqualTo(deity.ordinal)
          }
        }
      }

      context("isOneOf assertion") {
        test("Passes if the subject is one of the specified values") {
          expectThat(Pantheon.NORSE).isOneOf(Pantheon.NORSE, Pantheon.GREEK)
        }

        test("Fails if the subject is not one of the specified values") {
          expectThrows<AssertionFailedError> {
            expectThat(Pantheon.NORSE).isOneOf(Pantheon.ROMAN, Pantheon.GREEK)
          }
        }
      }
    }
}

enum class Pantheon(val ruler: String, val underworldRuler: String) {
  NORSE("Odin", "Hel"),
  GREEK("Zeus", "Hades"),
  ROMAN("Jupiter", "Pluto")
}

enum class Deity(val realm: Pantheon) {
  Eris(Pantheon.GREEK)
}
