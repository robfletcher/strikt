package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.catching
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.first
import strikt.assertions.flatMap
import strikt.assertions.get
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.last
import strikt.assertions.map
import strikt.assertions.message
import strikt.assertions.throws
import java.time.LocalDate

@DisplayName("mapping assertions")
internal class Mapping {
  @Test
  fun `map() on iterable subjects maps to an iterable`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expectThat(subject)
      .map { it.toUpperCase() }
      .containsExactly("CATFLAP", "RUBBERPLANT", "MARZIPAN")
  }

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

  @Test
  fun `first maps an iterable to its first element`() {
    val subject = listOf("catflap", "rubberplant", "marzipan", "radish")
    expectThat(subject)
      .first { it.startsWith("r") }
      .isEqualTo("rubberplant")
  }

  @Test
  fun `flatMap maps a result iterable to a flattened iterable`() {
    val subject = listOf(
      mapOf("words" to listOf("catflap", "rubberplant", "marzipan")),
      mapOf("words" to listOf("kattenluik", "rubberboom", "marsepein"))
    )
    expectThat(subject)
      .flatMap { it["words"]!! }
      .containsExactly(
        "catflap",
        "rubberplant",
        "marzipan",
        "kattenluik",
        "rubberboom",
        "marsepein"
      )
  }

  data class Person(val name: String, val birthDate: LocalDate)

  @Nested
  @DisplayName("custom mappings")
  inner class Custom {
    val subject = Person("David", LocalDate.of(1947, 1, 8))

    @Test
    fun `can map with a closure`() {
      expectThat(subject) {
        get { name }.isEqualTo("David")
        get { birthDate.year }.isEqualTo(1947)
      }
    }

    @Test
    fun `can map with property and method references`() {
      expectThat(subject) {
        get(Person::name).isEqualTo("David")
        get(Person::birthDate).get(LocalDate::getYear).isEqualTo(1947)
      }
    }

    @Test
    fun `closures can call methods`() {
      expectThat(subject) {
        get { name.toUpperCase() }.isEqualTo("DAVID")
        get { birthDate.plusYears(69).plusDays(2) }
          .isEqualTo(LocalDate.of(2016, 1, 10))
      }
    }

    @Test
    fun `can be described`() {
      fails {
        expectThat(subject) {
          get { name }.describedAs("name").isEqualTo("Ziggy")
          get { birthDate.year }.describedAs("birth year")
            .isEqualTo(1971)
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
        expectThat(subject).get(Person::name).isEqualTo("Ziggy")
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
          get { name }.isEqualTo("Ziggy")
          get {
            birthDate.year
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
        expectThat(subject).get(Person::birthDate)
          .get(LocalDate::getYear)
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

data class Person(val name: String, val birthDate: LocalDate = LocalDate.now())
data class Album(val name: String)
