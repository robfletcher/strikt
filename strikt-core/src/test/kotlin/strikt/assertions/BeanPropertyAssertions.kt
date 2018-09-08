package strikt.assertions

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.fails
import strikt.internal.reporting.toHex
import java.time.LocalDate
import java.util.Base64
import java.util.UUID

internal class BeanPropertyAssertions {

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
      image = randomBytes()
    )
    val other = subject.copy(
      name = "Ziggy",
      dateOfBirth = LocalDate.of(1972, 2, 10)
    )
    fails {
      expectThat(subject).propertiesAreEqualTo(other)
    }.let { error ->
      expectThat(error.message) {
        isNotNull()
        isEqualTo(
          "▼ Expect that Person(David):\n" +
            "  ✗ is equal field-by-field to Person(Ziggy)\n" +
            "    ▼ value of property dateOfBirth:\n" +
            "      ✗ is equal to 1972-02-10 : found 1947-01-08\n" +
            "    ▼ value of property id:\n" +
            "      ✓ is equal to ${subject.id}\n" +
            "    ▼ value of property image:\n" +
            "      ✓ array content equals 0x${subject.image.toHex()}\n" +
            "    ▼ value of property name:\n" +
            "      ✗ is equal to \"Ziggy\" : found \"David\""
        )
      }
    }
  }

  @Test
  fun `isEqualTo works with java fields that are null`() {
    val subject = PersonJava(null, null, null, null)
    fails {
      expect(subject.name).isEqualTo("Ziggy")
    }
  }

  @Test
  fun `can compare a Java POJO field-by-field`() {
    val subject = PersonJava(
      "David",
      LocalDate.of(1947, 1, 8),
      randomBytes()
    )
    val other = PersonJava(
      subject.id,
      "Ziggy",
      LocalDate.of(1972, 2, 10),
      subject.image
    )
    fails {
      expectThat(subject).propertiesAreEqualTo(other)
    }.let { error ->
      expectThat(error.message) {
        isNotNull()
        isEqualTo(
          "▼ Expect that Person(David):\n" +
            "  ✗ is equal field-by-field to Person(Ziggy)\n" +
            "    ▼ value of property dateOfBirth:\n" +
            "      ✗ is equal to 1972-02-10 : found 1947-01-08\n" +
            "    ▼ value of property id:\n" +
            "      ✓ is equal to ${subject.id}\n" +
            "    ▼ value of property image:\n" +
            "      ✓ array content equals 0x${subject.image.toHex()}\n" +
            "    ▼ value of property name:\n" +
            "      ✗ is equal to \"Ziggy\" : found \"David\""
        )
      }
    }
  }

  internal abstract class Animal(
    val name: String,
    val legs: Int,
    val tails: Int
  ) {
    override fun toString() = "${javaClass.simpleName}($name)"
  }

  internal class Cat(name: String, val breed: String, legs: Int, tails: Int) :
    Animal(name, legs, tails)

  @Test fun `considers inherited properties`() {
    val subject = Cat(name = "Oreo", breed = "Tuxedo", legs = 4, tails = 0)
    val other = Cat(name = "Rocky", breed = "Russian Blue", legs = 4, tails = 1)

    fails {
      expectThat(subject).propertiesAreEqualTo(other)
    }.let { error ->
      expectThat(error.message)
        .isNotNull()
        .isEqualTo(
          "▼ Expect that Cat(Oreo):\n" +
            "  ✗ is equal field-by-field to Cat(Rocky)\n" +
            "    ▼ value of property breed:\n" +
            "      ✗ is equal to \"Russian Blue\" : found \"Tuxedo\"\n" +
            "    ▼ value of property legs:\n" +
            "      ✓ is equal to 4\n" +
            "    ▼ value of property name:\n" +
            "      ✗ is equal to \"Rocky\" : found \"Oreo\"\n" +
            "    ▼ value of property tails:\n" +
            "      ✗ is equal to 1 : found 0"
        )
    }
  }
}

private fun ByteArray.base64() =
  Base64.getEncoder().encodeToString(this)
