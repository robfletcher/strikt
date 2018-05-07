package kirk.samples

import kirk.api.Assertion
import kirk.api.expect
import java.time.LocalDate
import java.time.temporal.ChronoUnit.YEARS
import kotlin.test.Test

data class Person(
  val name: String,
  val birthDate: LocalDate
)

class Assertions {
  @Test
  fun inlineAssert() {
    val subject = Person(
      name = "David",
      birthDate = LocalDate.of(1947, 1, 8)
    )
    expect(subject)
      .assert("is older than 21") {
        if (subject.birthDate < LocalDate.now().minus(21, YEARS)) {
          pass()
        } else {
          fail()
        }
      }
  }

  @Test
  fun assertionFunctionDefinition() {
    fun Assertion<Person>.isOlderThan(age: Int) =
      assert("is older than $age") {
        if (subject.birthDate < LocalDate.now().minus(age.toLong(), YEARS)) {
          pass()
        } else {
          fail()
        }
      }

    val subject = Person(
      name = "David",
      birthDate = LocalDate.of(1947, 1, 8)
    )
    expect(subject).isOlderThan(21)
  }
}
