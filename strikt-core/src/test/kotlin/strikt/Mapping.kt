package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.catching
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.first
import strikt.assertions.get
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.last
import strikt.assertions.message
import strikt.assertions.throws
import java.time.LocalDate

@DisplayName("mapping assertions")
internal class Mapping {
  @Test
  fun `first() maps to the first element of an iterable`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expectThat(subject).first().isEqualTo("catflap")
  }

  @Test
  fun `last() maps to the last element of an iterable`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expectThat(subject).last().isEqualTo("marzipan")
  }

  @Test
  fun `array access maps to an indexed element of a list`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expectThat(subject)[1].isEqualTo("rubberplant")
  }

  @Test
  fun `array access maps to a sub-list of a list`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expectThat(subject)[1..2].containsExactly("rubberplant", "marzipan")
  }

  @Test
  fun `array access maps to a value of a map`() {
    val subject = mapOf("foo" to "bar")
    expectThat(subject)["foo"].isNotNull().isEqualTo("bar")
    expectThat(subject)["bar"].isNull()
  }

  @Test
  fun `message maps to an exception message`() {
    expectThat(catching { error("o noes") })
      .throws<IllegalStateException>()
      .message
      .isEqualTo("o noes")
  }

  @Test
  fun `message fails if the exception message is null`() {
    fails {
      expectThat(catching { throw IllegalStateException() })
        .throws<IllegalStateException>()
        .message
    }.let { error ->
      expectThat(error).message.isEqualTo(
        "▼ Expect that java.lang.IllegalStateException:\n" +
          "  ✓ threw java.lang.IllegalStateException\n" +
          "  ▼ value of property message:\n" +
          "    ✗ is not null"
      )
    }
  }

  data class Person(val name: String, val birthDate: LocalDate)

  @Nested
  @DisplayName("custom mappings")
  inner class Custom {
    val subject = Person("David", LocalDate.of(1947, 1, 8))

    @Test
    fun `can map with a closure`() {
      expectThat(subject) {
        map { it.name }.isEqualTo("David")
        map { it.birthDate.year }.isEqualTo(1947)
      }
    }

    @Test
    fun `can map with property and method references`() {
      expectThat(subject) {
        map(Person::name).isEqualTo("David")
        map(Person::birthDate).map(LocalDate::getYear).isEqualTo(1947)
      }
    }

    @Test
    fun `closures can call methods`() {
      expectThat(subject) {
        map { it.name.toUpperCase() }.isEqualTo("DAVID")
        map { it.birthDate.plusYears(69).plusDays(2) }
          .isEqualTo(LocalDate.of(2016, 1, 10))
      }
    }

    @Test
    fun `can be described`() {
      fails {
        expectThat(subject) {
          map { it.name }.describedAs("name").isEqualTo("Ziggy")
          map { it.birthDate.year }.describedAs("birth year").isEqualTo(1971)
        }
      }.let { e ->
        assertEquals(
          "▼ Expect that Person(name=David, birthDate=1947-01-08):\n" +
            "  ▼ name:\n" +
            "    ✗ is equal to \"Ziggy\" : found \"David\"\n" +
            "  ▼ birth year:\n" +
            "    ✗ is equal to 1971 : found 1947",
          e.message
        )
      }
    }

    @Test
    fun `descriptions are defaulted when using property references`() {
      fails {
        expectThat(subject).map(Person::name).isEqualTo("Ziggy")
      }.let { e ->
        assertEquals(
          "▼ Expect that Person(name=David, birthDate=1947-01-08):\n" +
            "  ▼ value of property name:\n" +
            "    ✗ is equal to \"Ziggy\" : found \"David\"",
          e.message
        )
      }
    }

    @Test
    fun `descriptions also default for blocks`() {
      fails {
        expectThat(subject) {
          map { it.name }.isEqualTo("Ziggy")
          map {
            it.birthDate.year
          }.isEqualTo(1971)
        }
      }.let { e ->
        assertEquals(
          "▼ Expect that Person(name=David, birthDate=1947-01-08):\n" +
            "  ▼ name:\n" +
            "    ✗ is equal to \"Ziggy\" : found \"David\"\n" +
            "  ▼ birthDate.year:\n" +
            "    ✗ is equal to 1971 : found 1947",
          e.message
        )
      }
    }

    @Test
    fun `descriptions are defaulted when using bean getter references`() {
      fails {
        expectThat(subject).map(Person::birthDate).map(LocalDate::getYear)
          .isEqualTo(1971)
      }.let { e ->
        assertEquals(
          "▼ Expect that Person(name=David, birthDate=1947-01-08):\n" +
            "  ▼ value of property birthDate:\n" +
            "    ▼ return value of getYear:\n" + // TODO: treat as property ref
            "      ✗ is equal to 1971 : found 1947",
          e.message
        )
      }
    }
  }
}
