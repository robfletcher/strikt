package strikt.arrow

import arrow.core.Either
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
object EitherAssertions : JUnit5Minutests {

  fun tests() = rootContext {
    derivedContext<Assertion.Builder<Either<String, *>>>("leftEither") {
      fixture {
        expectThat(Either.Left("left"))
      }

      test("can assert on type") {
        isLeft()
      }

      test("can assert on type and value equality") {
        isLeft("left")
      }

      test("can assert on type and traverse unwrapped value") {
        isLeft().value isEqualTo "left"
      }
    }

    derivedContext<Assertion.Builder<Either<MyTuple, *>>>("leftEither") {
      fixture {
        expectThat(Either.Left(MyTuple("myName", 1, "uuid")))
      }

      test("can have nested assertions on unwrapped value") {
        isLeft().value.and {
          get { name } isEqualTo "myName"
          get { id }.isNotNull() isGreaterThan 0L
          get { uuid }.isNotNull().isNotBlank()
        }
      }
    }

    derivedContext<Assertion.Builder<Either<*, String>>>("rightEither") {
      fixture {
        expectThat(Either.Right("right"))
      }

      test("can assert on type") {
        isRight()
      }

      test("can assert on type and value equality") {
        isRight("right")
      }

      test("can assert on type and traverse unwrapped value") {
        isRight().value.isEqualTo("right")
      }
    }

    derivedContext<Assertion.Builder<Either<*, MyTuple>>>("rightEither") {
      fixture {
        expectThat(Either.Right(MyTuple("myName", 1, "uuid")))
      }

      test("can have nested assertions on unwrapped value") {
        isRight().value and {
          get { name } isEqualTo "myName"
          get { id }.isNotNull() isGreaterThan 0L
          get { uuid }.isNotNull().isNotBlank()
        }
      }
    }
  }
}
