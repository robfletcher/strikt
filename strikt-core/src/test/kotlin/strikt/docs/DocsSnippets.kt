package strikt.docs

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.Assertion
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.Pantheon
import strikt.assertions.any
import strikt.assertions.contains
import strikt.assertions.get
import strikt.assertions.hasLength
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEmpty
import strikt.assertions.isNotNull
import strikt.assertions.matches
import strikt.assertions.startsWith
import java.time.LocalDate
import java.time.MonthDay

@DisplayName("Snippets used")
internal class DocsSnippets {
  @Test
  fun `homepage one`() {
    // START homepage_one
    val subject = "The Enlightened take things Lightly"
    expectThat(subject)
      .hasLength(35)
      .matches(Regex("[\\w\\s]+"))
      .startsWith("T")
    // END homepage_one
  }

  @Test
  fun `homepage two, three`() {
    // START homepage_two
    val subject = listOf("Eris", "Thor", "Anubis", "Ra")
    expectThat(subject)
      .contains("Eris", "Thor", "Anubis")
    // END homepage_two
    // START homepage_three
    expectThat(subject)[0].isEqualTo("Eris")
    // END homepage_three
  }

  @Test
  fun `homepage four`() {
    // START homepage_four
    val subject = Pantheon.values().map { it.toString() }
    expectThat(subject)
      .isNotEmpty()
      .any { startsWith("E") }
    // END homepage_four
  }

  @Test
  fun `homepage five, six, seven`() {
    // START homepage_five
    val subject = "The Enlightened take things Lightly"
    expectThat(subject) {
      hasLength(5)          // fails
      matches(Regex("\\d+")) // fails
      startsWith("T")       // still evaluated and passes
    }
    // END homepage_five

    val s = """
    // START homepage_six
    ▼ Expect that "The Enlightened take things Lightly":
    ✗ has length 5 : found 35
    ✗ matches the regular expression /\d+/
    ✓ starts with "T"
    // END homepage_six
    """

    // START homepage_seven
    val person1 = Person("Eris")
    val person2 = Person("Thor")
    expect {
      that(person1).map { it.name }.isEqualTo("David")
      that(person2).map { it.name }.isEqualTo("Ziggy")
    }
    // END homepage_seven

  }

  @Test
  fun `homepage eight`() {
    // START homepage_eight
    val subject: Any? = "The Enlightened take things Lightly"
    expectThat(subject)            // type: Assertion<Any?>
      .isNotNull()                 // type: Assertion<Any>
      .isA<String>()               // type: Assertion<String>
      .matches(Regex("[\\w\\s]+"))
    // only available on Assertion<CharSequence>
    // END homepage_eight
  }

  @Test
  fun `homepage nine`() {
    // START homepage_nine
//    val subject = Pantheon.ERIS
//    expectThat(subject)
//      .map(Deity::realm)     // reference to a property
//      .map { it.toString() } // return type of a method call
//      .isEqualTo("discord and confusion")
    // END homepage_nine
  }

  @Test
  fun `homepage ten, eleven`() {
    // START homepage_ten
    fun Assertion.Builder<LocalDate>.isStTibsDay() =
    assert("is St. Tib's Day") {
      when (MonthDay.from(it)) {
        MonthDay.of(2, 29) -> pass()
        else               -> fail()
      }
    }
    expectThat(LocalDate.of(2018, 5, 15)).isStTibsDay()
    // END homepage_ten

    // START homepage_eleven
//    val Assertion.Builder<Deity>.realm: Assertion.Builder<String>
//    get() = map(Deity::realm)
//
//    val subject = Pantheon.ERIS
//    expectThat(subject)
//      .realm
//      .isEqualTo("discord and confusion")
    // END homepage_eleven
  }
}

class Person(val name: String)
