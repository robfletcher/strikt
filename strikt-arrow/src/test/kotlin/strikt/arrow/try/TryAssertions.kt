package strikt.arrow.`try`

import arrow.core.Try
import dev.minutest.junit.testFactoryFor
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isSameInstanceAs
import strikt.assertions.message

@DisplayName("assertions on Try")
object TryAssertions {

  @TestFactory
  fun trySuccess() = testFactoryFor(rootContext {
    val aTry = Try.just("success")

    test("can assert on type") {
      expectThat(aTry).isSuccess()
    }

    test("can negate assertion") {
      expectThat(aTry).not().isFailure()
    }

    test("can assert on type and value equality") {
      expectThat(aTry).isSuccess("success")
    }

    test("can assert on type and traverse value") {
      expectThat(aTry).isSuccess().value.isEqualTo("success")
    }
  })

  @TestFactory
  fun tryFailure() = testFactoryFor(rootContext {
    val aTry = Try.raiseError(IllegalArgumentException("testFailure"))

    test("can assert on type") {
      expectThat(aTry).isFailure()
    }

    test("can negate assertion") {
      expectThat(aTry).not().isSuccess()
    }

    test("can assert on type and traverse exception") {
      expectThat(aTry).isFailure().exception.message.isEqualTo("testFailure")
    }
  })
}
