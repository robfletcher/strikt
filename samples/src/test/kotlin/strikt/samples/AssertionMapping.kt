package strikt.samples

import org.junit.jupiter.api.Test
import strikt.api.Assertion
import strikt.api.expect
import strikt.assertions.*
import java.time.LocalDate

internal object AssertionMapping {

  @Test
  fun inlineMapping() {
    val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
    expect(subject) {
      map { name }.isEqualTo("David")
      map { birthDate }.map { year }.isEqualTo(1947)
    }
  }

  val Assertion<Person>.name: Assertion<String>
    get() = map(Person::name)

  val Assertion<Person>.birthDate: Assertion<LocalDate>
    get() = map(Person::birthDate)

  val Assertion<LocalDate>.year: Assertion<Int>
    get() = map(LocalDate::getYear)

  @Test
  fun extensionPropertyMapping() {
    val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
    expect(subject) {
      name.isEqualTo("David")
      birthDate.year.isEqualTo(1947)
    }
  }

  @Test
  fun suppliedMappingExtensionProperties() {
    val subject = listOf("catflap", "rubberplant", "marzipan")
    expect(subject).size.isGreaterThan(2)
    expect(subject).all {
      length.isGreaterThan(1)
    }
  }
}