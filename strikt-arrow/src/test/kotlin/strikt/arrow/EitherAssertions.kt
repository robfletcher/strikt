package strikt.arrow

import arrow.core.Either
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isNotBlank
import strikt.assertions.isNotNull

@DisplayName("assertions on Either")
object EitherAssertions : JUnit5Minutests {

  fun tests() = rootContext {
    context("leftEither") {

      val anEither = Either.left("left")

      test("can assert on type") {
        expectThat(anEither).isLeft()
      }

      test("can assert on type and value equality") {
        expectThat(anEither).isLeft("left")
      }

      test("can assert on type and traverse unwrapped value") {
        expectThat(anEither).isLeft().a.isEqualTo("left")
      }

      test("can have nested assertions on unwrapped value") {
        expectThat(Either.left(MyTuple("myName", 1, "uuid"))).isLeft().a.and {
          get { name } isEqualTo "myName"
          get { id }.isNotNull() isGreaterThan 0L
          get { uuid }.isNotNull().isNotBlank()
        }
      }
    }

    context("rightEither") {

      val anEither = Either.right("right")

      test("can assert on type") {
        expectThat(anEither).isRight()
      }

      test("can assert on type and value equality") {
        expectThat(anEither).isRight("right")
      }

      test("can assert on type and traverse unwrapped value") {
        expectThat(anEither).isRight().b.isEqualTo("right")
      }

      test("can have nested assertions on unwrapped value") {
        expectThat(Either.right(MyTuple("myName", 1, "uuid"))).isRight().b and {
          get { name } isEqualTo "myName"
          get { id }.isNotNull() isGreaterThan 0L
          get { uuid }.isNotNull().isNotBlank()
        }
      }
    }
  }
}
