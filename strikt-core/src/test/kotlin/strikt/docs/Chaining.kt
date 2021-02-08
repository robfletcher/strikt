package strikt.docs

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.Album
import strikt.Person
import strikt.api.Assertion
import strikt.api.expect
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.containsExactly
import strikt.assertions.first
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.last
import strikt.assertions.map
import strikt.assertions.message
import strikt.assertions.withFirst
import strikt.assertions.withLast
import strikt.internal.opentest4j.CompoundAssertionFailure
import java.time.LocalDate

@DisplayName("Snippets used in Orchid docs")
internal class Chaining {

// traversing-subjects.md
// -----------------------------------------------------------------------------

  @Test fun `traversing subjects 1`() {
    val map = mapOf("count" to 1)
    val list = listOf("fnord")
    val person = object {
      val name = "Ziggy"
    }

    // START traversing_subjects_1
    expectThat(map.size).isEqualTo(1)
    expectThat(list.first()).isEqualTo("fnord")
    expectThat(person.name).isEqualTo("Ziggy")
    // END traversing_subjects_1
  }

  @Test fun `traversing subjects 2, 3`() {
    val s =
      """
      // START traversing_subjects_3
      ▼ Expect that Person(name=David, birthDate=1947-01-08):
        ▼ name:
          ✗ is equal to "Ziggy"
                  found "David"
        ▼ birthDate.year:
          ✗ is equal to 1971
                  found 1947
      // END traversing_subjects_3
      """

    expectThrows<CompoundAssertionFailure> {
      // START traversing_subjects_2
      val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
      expectThat(subject) {
        get { name }.isEqualTo("Ziggy")
        get { birthDate.year }.isEqualTo(1971)
      }
      // END traversing_subjects_2
    }
      .message
      .isEqualTo(s.removeSnippetTags().trimIndent().trim())
  }

  @Test fun `traversing subjects 4`() {
    // START traversing_subjects_4
    val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
    expectThat(subject) {
      get(Person::name).isEqualTo("David")
      get(Person::birthDate).get(LocalDate::getYear).isEqualTo(1947)
    }
    // END traversing_subjects_4
  }

  @Test fun `traversing subjects 5`() {
    // START traversing_subjects_5
    val subject: List<Person> = getPersonList()
    expectThat(subject)
      .map(Person::name)
      .containsExactly("David", "Ziggy", "Aladdin", "Jareth")
    // END traversing_subjects_5
  }

  private fun getPersonList(): List<Person> {
    return listOf(
      Person("David"),
      Person("Ziggy"),
      Person("Aladdin"),
      Person("Jareth")
    )
  }

  // START traversing_subjects_6
  val Assertion.Builder<Person>.name: Assertion.Builder<String>
    get() = get(Person::name)

  val Assertion.Builder<Person>.yearOfBirth: Assertion.Builder<Int>
    get() = get("year of birth") { birthDate.year }
  // END traversing_subjects_6

  @Test fun `traversing subjects 7`() {
    // START traversing_subjects_7
    val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
    expectThat(subject) {
      name.isEqualTo("David")
      yearOfBirth.isEqualTo(1947)
    }
    // END traversing_subjects_7
  }

// grouping-with-and.md
// -----------------------------------------------------------------------------

  @Test fun `grouping with and 1`() {
    @Suppress("RedundantNullableReturnType")
    val subject: String? = "subject"
    // START grouping_with_and_1
    expectThat(subject)
      .isNotNull()
      .and {
        // perform other assertions on a known non-null subject
      }
    // END grouping_with_and_1
  }

  @Test fun `grouping with and 2, 3`() {
    val person = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
    // START grouping_with_and_2
    expectThat(person)
      .and {
        get { name }.isEqualTo("David")
      }
      .and {
        get { birthDate.year }.isEqualTo(1947)
      }
    // END grouping_with_and_2

    // START grouping_with_with_1
    expectThat(person)
      .with(Person::name) {
        isEqualTo("David")
      }
      .with({ birthDate.year }) {
        isEqualTo(1947)
      }
    // END grouping_with_with_1

    // START grouping_with_and_3
    expect {
      that(person.name).isEqualTo("David")
      that(person.birthDate.year).isEqualTo(1947)
    }
    // END grouping_with_and_3
  }

  @Test fun `grouping with and 4`() {
    val albums = listOf(
      Album("David Bowie"),
      *((0 until 24).map { Album("$it") }).toTypedArray(),
      Album("Blackstar")
    )
    // START grouping_with_and_4
    expectThat(albums)
      .hasSize(26)
      .and { first().get { name }.isEqualTo("David Bowie") }
      .and { last().get { name }.isEqualTo("Blackstar") }
    // END grouping_with_and_4

    // START grouping_with_with_2
    expectThat(albums)
      .hasSize(26)
      .withFirst {
        get { name }.isEqualTo("David Bowie")
      }
      .withLast {
        get { name }.isEqualTo("Blackstar")
      }
    // END grouping_with_with_2
  }
}
