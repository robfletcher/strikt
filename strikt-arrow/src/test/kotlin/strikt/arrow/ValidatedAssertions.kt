package strikt.arrow

import arrow.core.Validated
import arrow.core.invalid
import arrow.core.valid
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isNotBlank
import strikt.assertions.isNotNull

@DisplayName("assertions on Either")
object ValidatedAssertions : JUnit5Minutests {

  fun tests() = rootContext {
    derivedContext<Assertion.Builder<Validated<String, *>>>("invalidValidated") {
      fixture {
        expectThat("invalid".invalid())
      }

      test("can assert on type") {
        isInvalid()
      }

      test("can assert on type and value equality") {
        isInvalid("invalid")
      }

      test("can assert on type and traverse unwrapped value") {
        isInvalid().value isEqualTo "invalid"
      }
    }

    derivedContext<Assertion.Builder<Validated<MyTuple, *>>>("invalidValidated") {
      fixture {
        expectThat(MyTuple("myName", 1, "uuid").invalid())
      }

      test("can have nested assertions on unwrapped value") {
        isInvalid().value.and {
          get { name }.isEqualTo("myName")
          get { id }.isNotNull().isGreaterThan(0L)
          get { uuid }.isNotNull().isNotBlank()
        }
      }
    }

    derivedContext<Assertion.Builder<Validated<*, String>>>("validValidated") {
      fixture {
        expectThat("valid".valid())
      }

      test("can assert on type") {
        isValid()
      }

      test("can assert on type and value equality") {
        isValid("valid")
      }

      test("can assert on type and traverse unwrapped value") {
        isValid().value.isEqualTo("valid")
      }
    }

    derivedContext<Assertion.Builder<Validated<*, MyTuple>>>("validValidated") {
      fixture {
        expectThat(MyTuple("myName", 1, "uuid").valid())
      }

      test("can have nested assertions on unwrapped value") {
        isValid().value.and {
          get { name }.isEqualTo("myName")
          get { id }.isNotNull().isGreaterThan(0L)
          get { uuid }.isNotNull().isNotBlank()
        }
      }
    }
  }
}
