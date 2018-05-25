package strikt.samples

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.MultipleFailuresError
import strikt.api.Assertion
import strikt.api.expect
import strikt.assertions.any
import strikt.assertions.contains
import strikt.assertions.hasLength
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.matches
import strikt.assertions.startsWith
import java.time.LocalDate
import java.time.MonthDay

object Docs {

  @Test
  fun narrowing() {
    val subject: Any? = "The Enlightened take things Lightly"
    expect(subject)              // type: Assertion<Any?>
      .isNotNull()               // type: Assertion<Any>
      .isA<String>()             // type: Assertion<String>
      .matches(Regex("[\\w\\s]+")) // only available on Assertion<CharSequence>
  }

  @Test
  fun mapping() {
    val subject = Pantheon.ERIS
    expect(subject)
      .map(Deity::realm)  // type safe reference to a property narrows assertion
      .map { toString() } // narrows assertion to return type of method call
      .isEqualTo("discord and confusion")
  }

  @Test
  fun softAssertions() {
    assertThrows<MultipleFailuresError> {
      val subject = "The Enlightened take things Lightly"
      expect("", subject) {
        hasLength(5)           // fails
        matches(Regex("\\d+")) // fails
        startsWith("T")        // still evaluated and passes
      }
    }.let {
      assertEquals(
        """Multiple Failures (2 failures)
	has length 5 : found 35
	matches the regular expression /\d+/""",
        it.message
      )
    }
  }

  @Test
  fun customAssertions() {
    assertThrows<MultipleFailuresError> {
      fun Assertion<LocalDate>.isStTibsDay() =
        assert("is St. Tib's Day") {
          when (MonthDay.from(subject)) {
            MonthDay.of(2, 29) -> pass()
            else -> fail()
          }
        }

      expect(LocalDate.of(2018, 5, 15)).isStTibsDay()
    }.let {
      assertEquals(
        """Expect that 2018-05-15 (1 failure)
	is St. Tib's Day""",
        it.message
      )
    }
  }

  val Assertion<Deity>.realm: Assertion<String>
    get() = map(Deity::realm)

  @Test
  fun customNarrowing() {
    val subject = Pantheon.ERIS
    expect(subject).realm.isEqualTo("discord and confusion")
  }

  val Assertion<Deity>.culture: Assertion<String>
    get() = map(Deity::culture)

  val Assertion<Deity>.aliases: Assertion<List<String>>
    get() = map(Deity::aliases)

  @Test
  fun complexAssertion() {
    val subject = Pantheon.values()
    expect(subject).any {
      culture.isEqualTo("Grœco-Californian")
      realm.isEqualTo("discord and confusion")
      aliases.contains("Discordia")
    }.let {
      assertEquals(
        """▼ Expect that [Eris, Thor]
  ✓ at least one element matches:
    ▼ Expect that Eris
      ▼ .culture "Grœco-Californian"
        ✓ is equal to "Grœco-Californian"
      ▼ .realm "discord and confusion"
        ✓ is equal to "discord and confusion"
      ▼ .aliases ["Ἔρις", "Discordia"]
        ✓ contains the elements ["Discordia"]
          ▼ Expect that ["Ἔρις", "Discordia"]
            ✓ contains "Discordia"
    ▼ Expect that Thor
      ▼ .culture "Norse"
        ✗ is equal to "Grœco-Californian"
      ▼ .realm "thunder"
        ✗ is equal to "discord and confusion"
      ▼ .aliases ["Þórr", "Þunor"]
        ✗ contains the elements ["Discordia"]
          ▼ Expect that ["Þórr", "Þunor"]
            ✗ contains "Discordia"
""", it.writeReport()
      )
    }
  }
}

data class Deity(
  val name: String,
  val realm: String,
  val culture: String,
  val aliases: List<String>
) {
  override fun toString(): String = name
}

object Pantheon {
  val ERIS = Deity("Eris", "discord and confusion", "Grœco-Californian", listOf("Ἔρις", "Discordia"))
  val THOR = Deity("Thor", "thunder", "Norse", listOf("Þórr", "Þunor"))

  fun values() = listOf(ERIS, THOR)
}