package strikt.arrow.`try`

import arrow.core.Try
import dev.minutest.junit.testFactoryFor
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo

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

    test("can assert on type and with custom predicate") {
      expectThat(aTry).isSuccessWhere { it == "success" }
    }

    test("can chain assertion on narrowed type") {
      expectThat(aTry)
        .isSuccess()
        .get { value }
        .isEqualTo("success")
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

    test("can assert on type and with custom predicate") {
      expectThat(aTry).isFailureWhere { it.message == "testFailure" }
    }

    test("can chain assertion on narrowed type") {
      expectThat(aTry)
        .isFailure()
        .get { exception.message }
        .isEqualTo("testFailure")
    }

    /*
        it("can chain on narrowed type") {
            expectThat(aTry)
                .isFailureOfType<IllegalArgumentException>()
        }
         */
  })
}
