package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.containsExactly
import strikt.assertions.first
import strikt.assertions.get
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.last
import java.time.LocalDate

@DisplayName("mapping assertions")
internal class Mapping {
  @Test
  fun `first() maps to the first element of an iterable`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expect(subject).first().isEqualTo("catflap")
  }

  @Test
  fun `last() maps to the last element of an iterable`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expect(subject).last().isEqualTo("marzipan")
  }

  @Test
  fun `array access maps to an indexed element of a list`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expect(subject)[1].isEqualTo("rubberplant")
  }

  @Test
  fun `array access maps to a sub-list of a list`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expect(subject)[1..2].containsExactly("rubberplant", "marzipan")
  }

  @Test
  fun `array access maps to a value of a map`() {
    val subject = mapOf("foo" to "bar")
    expect(subject)["foo"].isNotNull().isEqualTo("bar")
    expect(subject)["bar"].isNull()
  }

  data class Person(val name: String, val birthDate: LocalDate)

  @Nested
  @DisplayName("custom mappings")
  inner class Custom {
    val subject = Person("David", LocalDate.of(1947, 1, 8))

    @Test
    fun `can map with a closure`() {
      expect(subject) {
        map { name }.isEqualTo("David")
        map { birthDate.year }.isEqualTo(1947)
      }
    }

    @Test
    fun `can map with property and method references`() {
      expect(subject) {
        map(Person::name).isEqualTo("David")
        map(Person::birthDate).map(LocalDate::getYear).isEqualTo(1947)
      }
    }

    @Test
    fun `closures can call methods`() {
      expect(subject) {
        map { name.toUpperCase() }.isEqualTo("DAVID")
        map { birthDate.plusYears(69).plusDays(2) }.isEqualTo(LocalDate.of(2016, 1, 10))
      }
    }

    @Test
    fun `can be described`() {
      fails {
        expect(subject) {
          map { name }.describedAs("name").isEqualTo("Ziggy")
          map { birthDate.year }.describedAs("birth year").isEqualTo(1971)
        }
      }.let { e ->
        val expectedMessage = listOf(
          "Expect that: Person(name=David, birthDate=1947-01-08) (2 failures)",
          "\tExpect that: name (1 failure)",
          "\tis equal to \"Ziggy\" : found \"David\"",
          "\tExpect that: birth year (1 failure)",
          "\tis equal to 1971 : found 1947"
        )
        assertEquals(expectedMessage, e.message?.lines())
      }
    }

    @Test
    fun `descriptions are defaulted when using property references`() {
      fails {
        expect(subject).map(Person::name).isEqualTo("Ziggy")
      }.let { e ->
        val expectedMessage = listOf(
          "Expect that: Person(name=David, birthDate=1947-01-08) (1 failure)",
          "\tExpect that: .name \"${subject.name}\" (1 failure)",
          "\tis equal to \"Ziggy\" : found \"David\""
        )
        assertEquals(expectedMessage, e.message?.lines())
      }
    }

    @Test
    fun `descriptions are defaulted when using bean getter references`() {
      fails {
        expect(subject).map(Person::birthDate).map(LocalDate::getYear).isEqualTo(1971)
      }.let { e ->
        val expectedMessage = listOf(
          "Expect that: Person(name=David, birthDate=1947-01-08) (1 failure)",
          "\tExpect that: .birthDate ${subject.birthDate} (1 failure)",
          "\tExpect that: .year ${subject.birthDate.year} (1 failure)", // TODO: can we make it indent again?
          "\tis equal to 1971 : found 1947"
        )
        assertEquals(expectedMessage, e.message?.lines())
      }
    }
  }
}
