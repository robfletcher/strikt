package strikt.arrow

import arrow.core.invalid
import arrow.core.valid
import dev.minutest.junit.testFactoryFor
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isNotBlank
import strikt.assertions.isNotNull

@DisplayName("assertions on Either")
object ValidatedAssertions {

  @TestFactory
  fun invalidValidated() = testFactoryFor(rootContext<Unit> {
    val aValidated = "invalid".invalid()

    test("can assert on type") {
      expectThat(aValidated).isInvalid()
    }

    test("can assert on type and value equality") {
      expectThat(aValidated).isInvalid("invalid")
    }

    test("can assert on type and traverse unwrapped value") {
      expectThat(aValidated).isInvalid().e.isEqualTo("invalid")
    }

    test("can have nested assertions on unwrapped value") {
      expectThat(MyTuple("myName", 1, "uuid").invalid()).isInvalid().e.and {
        get { name }.isEqualTo("myName")
        get { id }.isNotNull().isGreaterThan(0L)
        get { uuid }.isNotNull().isNotBlank()
      }
    }
  })

  @TestFactory
  fun validValidated() = testFactoryFor(rootContext<Unit> {
    val aValidated = "valid".valid()

    test("can assert on type") {
      expectThat(aValidated).isValid()
    }

    test("can assert on type and value equality") {
      expectThat(aValidated).isValid("valid")
    }

    test("can assert on type and traverse unwrapped value") {
      expectThat(aValidated).isValid().a.isEqualTo("valid")
    }

    test("can have nested assertions on unwrapped value") {
      expectThat(MyTuple("myName", 1, "uuid").valid()).isValid().a.and {
        get { name }.isEqualTo("myName")
        get { id }.isNotNull().isGreaterThan(0L)
        get { uuid }.isNotNull().isNotBlank()
      }
    }
  })
}
