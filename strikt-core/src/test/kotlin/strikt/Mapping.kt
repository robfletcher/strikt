package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import strikt.api.expect
import strikt.assertions.containsExactly
import strikt.assertions.first
import strikt.assertions.get
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.last
import java.time.LocalDate

internal object Mapping : Spek({
  describe("standard library mappings") {
    describe("mapping assertions on ${Iterable::class.java.simpleName}") {
      it("maps to the first element using first") {
        val subject = listOf("catflap", "rubberplant", "marzipan")
        expect(subject).first().isEqualTo("catflap")
      }

      it("maps to the last element using first") {
        val subject = listOf("catflap", "rubberplant", "marzipan")
        expect(subject).last().isEqualTo("marzipan")
      }
    }

    describe("mapping assertions on ${List::class.java.simpleName}") {
      it("maps to an indexed element using [int]") {
        val subject = listOf("catflap", "rubberplant", "marzipan")
        expect(subject)[1].isEqualTo("rubberplant")
      }

      it("maps to a ranged sub-list using [IntRange]") {
        val subject = listOf("catflap", "rubberplant", "marzipan")
        expect(subject)[1..2].containsExactly("rubberplant", "marzipan")
      }
    }

    describe("mapping assertions on ${Map::class.java.simpleName}") {
      it("maps to a value element using [key]") {
        val subject = mapOf("foo" to "bar")
        expect(subject)["foo"].isNotNull().isEqualTo("bar")
        expect(subject)["bar"].isNull()
      }
    }
  }

  describe("custom mapping") {
    data class Person(val name: String, val birthDate: LocalDate)

    val subject = Person("David", LocalDate.of(1947, 1, 8))

    it("maps the assertion subject to the closure result") {
      expect(subject) {
        map { name }.isEqualTo("David")
        map { birthDate.year }.isEqualTo(1947)
      }
    }

    it("can use property syntax") {
      expect(subject) {
        map(Person::name).isEqualTo("David")
        map(Person::birthDate).map(LocalDate::getYear).isEqualTo(1947)
      }
    }

    it("allows methods to be called") {
      expect(subject) {
        map { name.toUpperCase() }.isEqualTo("DAVID")
        map { birthDate.plusYears(69).plusDays(2) }.isEqualTo(LocalDate.of(2016, 1, 10))
      }
    }

    it("can describe the mapping") {
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

    it("automatically maps a Kotlin property name to the downstream subject description") {
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

    it("automatically maps a Java property name to the downstream subject description") {
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
})
