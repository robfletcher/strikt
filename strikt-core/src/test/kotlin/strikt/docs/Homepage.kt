package strikt.docs

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.Person
import strikt.api.Assertion
import strikt.api.expect
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.Deity
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
import strikt.assertions.message
import strikt.assertions.startsWith
import strikt.internal.opentest4j.CompoundAssertionFailure
import java.time.LocalDate
import java.time.MonthDay

// Wrap each code snippet in comments like "// START (snippet name)...// END (snippet name)"
// Code snippets can be referenced from the docs using the {% snippet %} tag
// (see https://orchid.run/plugins/orchidsnippets for snippets docs)
@DisplayName("Snippets used in Orchid docs")
internal class Homepage {
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
    val subject = Deity.entries.map { it.toString() }
    expectThat(subject)
      .isNotEmpty()
      .any { startsWith("E") }
    // END homepage_four
  }

  @Suppress("ktlint:standard:no-multi-spaces")
  @Test
  fun `homepage five, six, seven`() {
    val s =
      """
      // START homepage_six
      ▼ Expect that "The Enlightened take things Lightly":
        ✗ has length 5
               found 35
        ✗ matches the regular expression /\d+/
                                   found "The Enlightened take things Lightly"
        ✓ starts with "T"
      // END homepage_six
      """

    expectThrows<CompoundAssertionFailure> {
      // START homepage_five
      val subject = "The Enlightened take things Lightly"
      expectThat(subject) {
        hasLength(5)           // fails
        matches(Regex("\\d+")) // fails
        startsWith("T")        // still evaluated and passes
      }
      // END homepage_five
    }
      .message
      .isEqualTo(s.removeSnippetTags().trimIndent().trim())
  }

  @Test
  fun `homepage seven`() {
    // START homepage_seven
    val person1 = Person(name = "David")
    val person2 = Person(name = "Ziggy")
    expect {
      that(person1.name).isEqualTo("David")
      that(person2.name).isEqualTo("Ziggy")
    }
    // END homepage_seven
  }

  @Suppress("ktlint:standard:no-multi-spaces", "RedundantNullableReturnType")
  @Test
  fun `homepage eight`() {
    // START homepage_eight
    val subject: Any? = "The Enlightened take things Lightly"
    expectThat(subject) // type: Assertion.Builder<Any?>
      .isNotNull()      // type: Assertion.Builder<Any>
      .isA<String>()    // type: Assertion.Builder<String>
      // only available on Assertion.Builder<CharSequence>
      .matches(Regex("[\\w\\s]+"))
    // END homepage_eight
  }

  @Suppress("ktlint:standard:no-multi-spaces")
  @Test
  fun `homepage nine`() {
    // START homepage_nine
    val subject = Pantheon.NORSE
    expectThat(subject)
      .get(Pantheon::ruler) // reference to a property
      .get { toString() }   // return type of a method call
      .isEqualTo("Odin")
    // END homepage_nine
  }

  @Test
  fun `homepage ten, eleven`() {
    // START homepage_ten
    fun Assertion.Builder<LocalDate>.isStTibsDay() =
      assert("is St. Tib's Day") {
        when (MonthDay.from(it)) {
          MonthDay.of(2, 29) -> pass()
          else -> fail()
        }
      }
    expectThat(LocalDate.of(2020, 2, 29)).isStTibsDay()
    // END homepage_ten
  }

  // START homepage_eleven_a
  val Assertion.Builder<Pantheon>.realm: Assertion.Builder<String>
    get() = get { "$ruler to $underworldRuler" }
  // END homepage_eleven_a

  @Test
  fun `homepage eleven`() {
    // START homepage_eleven_b
    val subject = Pantheon.NORSE
    expectThat(subject)
      .realm
      .isEqualTo("Odin to Hel")
    // END homepage_eleven_b
  }
}
