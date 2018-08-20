package strikt.assertions

import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.fails
import strikt.internal.reporting.toHex
import java.time.LocalDate
import java.util.Base64
import java.util.UUID

internal class PojoAssertions {

  internal data class Person(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val dateOfBirth: LocalDate,
    val image: ByteArray
  ) {
    // override because I want to test that assertions don't just use ==
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false
      other as Person
      return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString() = "Person($name)"
  }

  @Test
  fun `can compare a Kotlin data class field-by-field`() {
    val subject = Person(
      name = "David",
      dateOfBirth = LocalDate.of(1947, 1, 8),
      image = randomBytes()
    )
    val other = subject.copy(
      name = "Ziggy",
      dateOfBirth = LocalDate.of(1972, 2, 10)
    )
    fails {
      expect(subject).allPropertiesAreEqualTo(other)
    }.let { error ->
      expect(error.message) {
        isNotNull()
          .and {
            isEqualTo(
              "▼ Expect that Person(David):\n" +
                "  ✗ is equal field-by-field to Person(Ziggy)\n" +
                "    ▼ value of property dateOfBirth:\n" +
                "      ✗ is equal to 1972-02-10 : found 1947-01-08\n" +
                "    ▼ value of property id:\n" +
                "      ✓ is equal to ${subject.id}\n" +
                "    ▼ value of property image:\n" +
                "      ✓ is equal to 0x${subject.image.toHex()}\n" +
                "    ▼ value of property name:\n" +
                "      ✗ is equal to \"Ziggy\" : found \"David\""
            )
          }
      }
    }
  }
}

private fun ByteArray.base64() =
  Base64.getEncoder().encodeToString(this)
