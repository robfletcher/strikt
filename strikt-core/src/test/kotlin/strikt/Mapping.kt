package strikt

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.containsExactly
import strikt.assertions.elementAt
import strikt.assertions.filter
import strikt.assertions.filterIsInstance
import strikt.assertions.filterNot
import strikt.assertions.first
import strikt.assertions.flatMap
import strikt.assertions.get
import strikt.assertions.isEqualTo
import strikt.assertions.isIn
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.last
import strikt.assertions.map
import strikt.assertions.message
import strikt.assertions.single
import java.time.LocalDate
import kotlin.random.Random

@DisplayName("mapping assertions")
internal class Mapping {
  @Test
  fun `get perf`() {
    class Person(
      val id: Int = Random.nextInt(),
      val id1: Int = Random.nextInt(),
      val id2: Int = Random.nextInt(),
      val id3: Int = Random.nextInt(),
      val id4: Int = Random.nextInt(),
      val id5: Int = Random.nextInt(),
      val id6: Int = Random.nextInt(),
      val id7: Int = Random.nextInt(),
      val id8: Int = Random.nextInt(),
      val id9: Int = Random.nextInt(),
      val id10: Int = Random.nextInt(),
      val id11: Int = Random.nextInt(),
      val id12: Int = Random.nextInt(),
      val id13: Int = Random.nextInt(),
      val id14: Int = Random.nextInt(),
      val id15: Int = Random.nextInt(),
      val id16: Int = Random.nextInt(),
      val id17: Int = Random.nextInt(),
      val id18: Int = Random.nextInt(),
      val id19: Int = Random.nextInt(),
      val id20: Int = Random.nextInt(),
      val id21: Int = Random.nextInt(),
      val id22: Int = Random.nextInt(),
      val id23: Int = Random.nextInt(),
      val id24: Int = Random.nextInt(),
      val id25: Int = Random.nextInt(),
      val id26: Int = Random.nextInt(),
      val id27: Int = Random.nextInt(),
    )
    val range = Int.MIN_VALUE..Int.MAX_VALUE

    repeat(10_000) {
      expectThat(Person(Random.nextInt())) {
        get { id } isIn range
        get { id1 } isIn range
        get { id2 } isIn range
        get { id3 } isIn range
        get { id4 } isIn range
        get { id5 } isIn range
        get { id6 } isIn range
        get { id7 } isIn range
        get { id8 } isIn range
        get { id9 } isIn range
        get { id10 } isIn range
        get { id11 } isIn range
        get { id12 } isIn range
        get { id13 } isIn range
        get { id14 } isIn range
        get { id15 } isIn range
        get { id16 } isIn range
        get { id17 } isIn range
        get { id18 } isIn range
        get { id19 } isIn range
        get { id20 } isIn range
        get { id21 } isIn range
        get { id22 } isIn range
        get { id23 } isIn range
        get { id24 } isIn range
        get { id25 } isIn range
        get { id26 } isIn range
        get { id27 } isIn range
      }
    }
  }

  @Test
  fun `map() on iterable subjects maps to an iterable`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expectThat(subject)
      .map { it.uppercase() }
      .containsExactly("CATFLAP", "RUBBERPLANT", "MARZIPAN")
  }

  @Test
  fun `first() maps to the first element of an iterable`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expectThat(subject).first().isEqualTo("catflap")
  }

  @Test
  fun `elementAt maps an iterable to the indexed element`() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expectThat(subject).elementAt(1).isEqualTo("rubberplant")
  }

  @Test
  fun `single() maps to the single element of an iterable`() {
    val subject = listOf("catflap")
    expectThat(subject).single().isEqualTo("catflap")
  }

  @Test
  fun `single() fails when the iterable has no elements`() {
    val subject = emptyList<String>()
    assertThrows<AssertionError> {
      expectThat(subject).single().isEqualTo("catflap")
    }.let { error ->
      expectThat(error).message.isEqualTo(
        """▼ Expect that []:
          |  ✗ has only one element
          |    found []"""
          .trimMargin()
      )
    }
  }

  @Test
  fun `single() fails when the iterable has multiple elements`() {
    val subject = listOf("catflap", "rubberplant")
    assertThrows<AssertionError> {
      expectThat(subject).single().isEqualTo("catflap")
    }.let { error ->
      expectThat(error).message.isEqualTo(
        """▼ Expect that ["catflap", "rubberplant"]:
          |  ✗ has only one element
          |    found ["catflap", "rubberplant"]"""
          .trimMargin()
      )
    }
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
  fun `filter reduces a subject iterable`() {
    val subject = listOf(1, 2, 3, 4)
    expectThat(subject)
      .filter { it >= 2 }
      .containsExactly(2, 3, 4)
  }

  @Test
  fun `filterNot reduces a subject iterable`() {
    val subject = listOf(1, 2, 3, 4)
    expectThat(subject)
      .filterNot { it > 2 }
      .containsExactly(1, 2)
  }

  @Test
  fun `filterIsInstance reduces a subject iterable`() {
    val subject = listOf(1, 2L, 3.0, 4)
    expectThat(subject)
      .filterIsInstance<Int>()
      .containsExactly(1, 4)
  }

  @Test
  fun `message maps to an exception message`() {
    expectThrows<IllegalStateException> {
      check(false) { "o noes" }
    }
      .message
      .isEqualTo("o noes")
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
        get { name.uppercase() }.isEqualTo("DAVID")
        get { birthDate.plusYears(69).plusDays(2) }
          .isEqualTo(LocalDate.of(2016, 1, 10))
      }
    }

    @Test
    fun `can be described`() {
      val error = assertThrows<AssertionError> {
        expectThat(subject)
          .describedAs { "a person named $name" }
          .and {
            get { name }.describedAs("name").isEqualTo("Ziggy")
            get { birthDate.year }.describedAs("birth year")
              .isEqualTo(1971)
          }
      }
      expectThat(error.message).isEqualTo(
        """▼ Expect that a person named David:
          |  ▼ name:
          |    ✗ is equal to "Ziggy"
          |            found "David"
          |  ▼ birth year:
          |    ✗ is equal to 1971
          |            found 1947""".trimMargin()
      )
    }

    @Test
    fun `descriptions are defaulted when using property references`() {
      val error = assertThrows<AssertionError> {
        expectThat(subject).get(Person::name).isEqualTo("Ziggy")
      }
      expectThat(error.message).isEqualTo(
        """▼ Expect that Person(name=David, birthDate=1947-01-08):
          |  ▼ value of property name:
          |    ✗ is equal to "Ziggy"
          |            found "David"""".trimMargin()
      )
    }

    @Test
    fun `descriptions also default for blocks`() {
      val error = assertThrows<AssertionError> {
        expectThat(subject) {
          get { name }.isEqualTo("Ziggy")
          get { birthDate.year }.isEqualTo(1971)
        }
      }
      expectThat(error.message).isEqualTo(
        """▼ Expect that Person(name=David, birthDate=1947-01-08):
          |  ▼ name:
          |    ✗ is equal to "Ziggy"
          |            found "David"
          |  ▼ birthDate.year:
          |    ✗ is equal to 1971
          |            found 1947""".trimMargin()
      )
    }

    @Test
    fun `descriptions are defaulted when using bean getter references`() {
      val error = assertThrows<AssertionError> {
        expectThat(subject).get(Person::birthDate)
          .get(LocalDate::getYear)
          .isEqualTo(1971)
      }
      expectThat(error.message).isEqualTo(
        """▼ Expect that Person(name=David, birthDate=1947-01-08):
          |  ▼ value of property birthDate:
          |    ▼ return value of getYear:
          |      ✗ is equal to 1971
          |              found 1947""".trimMargin()
      )
    }
  }
}

data class Person(val name: String, val birthDate: LocalDate = LocalDate.now())
data class Album(val name: String)
