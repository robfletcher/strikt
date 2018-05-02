package assertions

import assertions.api.expect
import assertions.assertions.isEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import java.time.LocalDate

internal object Mapping : Spek({
  describe("mapping over an assertion") {
    data class Person(val name: String, val birthDate: LocalDate)

    val subject = Person("David", LocalDate.of(1947, 1, 8))

    it("maps the assertion subject to the closure result") {
      expect(subject) {
        map { name }.isEqualTo("David")
        map { birthDate }.map { year }.isEqualTo(1947)
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
  }
})