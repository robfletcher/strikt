package strikt.java

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isTrue
import java.time.LocalDate
import java.util.UUID

internal object BeanPropertyAssertions {

  internal data class PersonKotlin(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val dateOfBirth: LocalDate,
    val image: ByteArray
  ) {
    // override because I want to test that assertions don't just use ==
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false
      other as PersonKotlin
      return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString() = "Person($name)"
  }

  @Test
  fun `can compare a Kotlin data class field-by-field`() {
    val subject = PersonKotlin(
      name = "David",
      dateOfBirth = LocalDate.of(1947, 1, 8),
      image = "catflap".toByteArray()
    )
    val other = subject.copy(
      name = "Ziggy",
      dateOfBirth = LocalDate.of(1972, 2, 10)
    )
    val error = assertThrows<AssertionError> {
      expectThat(subject).propertiesAreEqualTo(other)
    }
    expectThat(error.message) {
      isNotNull()
      isEqualTo(
        """▼ Expect that Person(David):
          |  ✗ is equal field-by-field to Person(Ziggy)
          |    ▼ value of property dateOfBirth:
          |      ✗ is equal to 1972-02-10
          |              found 1947-01-08
          |    ▼ value of property id:
          |      ✓ is equal to ${subject.id}
          |    ▼ value of property image:
          |      ✓ array content equals 0x636174666C6170
          |    ▼ value of property name:
          |      ✗ is equal to "Ziggy"
          |              found "David"""".trimMargin()
      )
    }
  }

  @Test
  fun `isEqualTo works with java fields that are null`() {
    val subject = PersonJava(null, null, null, null)
    expectThat(
      assertThrows<AssertionError> {
        expectThat(subject.name).isEqualTo("Ziggy")
      }
    ).isA<AssertionFailedError>().and {
      get { actual.value }.isNull()
      get { isActualDefined }.isTrue()
    }
  }

  @Test
  fun `can compare a Java POJO field-by-field`() {
    val subject = PersonJava(
      "David",
      LocalDate.of(1947, 1, 8),
      "catflap".toByteArray()
    )
    val other = PersonJava(
      subject.id,
      "Ziggy",
      LocalDate.of(1972, 2, 10),
      subject.image
    )
    val error = assertThrows<AssertionError> {
      expectThat(subject).propertiesAreEqualTo(other)
    }
    expectThat(error.message) {
      isNotNull()
      isEqualTo(
        """▼ Expect that Person(David):
          |  ✗ is equal field-by-field to Person(Ziggy)
          |    ▼ value of property dateOfBirth:
          |      ✗ is equal to 1972-02-10
          |              found 1947-01-08
          |    ▼ value of property id:
          |      ✓ is equal to ${subject.id}
          |    ▼ value of property image:
          |      ✓ array content equals 0x636174666C6170
          |    ▼ value of property name:
          |      ✗ is equal to "Ziggy"
          |              found "David"""".trimMargin()
      )
    }
  }

  @Suppress("unused", "MemberVisibilityCanBePrivate")
  internal abstract class Animal(
    val name: String,
    val legs: Int,
    val tails: Int
  ) {
    override fun toString() = "${javaClass.simpleName}($name)"
  }

  @Suppress("unused")
  internal class Cat(name: String, val breed: String, legs: Int, tails: Int) :
    Animal(name, legs, tails)

  @Test fun `considers inherited properties`() {
    val subject = Cat(name = "Oreo", breed = "Tuxedo", legs = 4, tails = 0)
    val other = Cat(name = "Rocky", breed = "Russian Blue", legs = 4, tails = 1)

    assertThrows<AssertionError> {
      expectThat(subject).propertiesAreEqualTo(other)
    }.let { error ->
      expectThat(error.message)
        .isNotNull()
        .isEqualTo(
          """▼ Expect that Cat(Oreo):
            |  ✗ is equal field-by-field to Cat(Rocky)
            |    ▼ value of property breed:
            |      ✗ is equal to "Russian Blue"
            |              found "Tuxedo"
            |    ▼ value of property legs:
            |      ✓ is equal to 4
            |    ▼ value of property name:
            |      ✗ is equal to "Rocky"
            |              found "Oreo"
            |    ▼ value of property tails:
            |      ✗ is equal to 1
            |              found 0""".trimMargin()
        )
    }
  }
}
