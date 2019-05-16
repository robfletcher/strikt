package strikt.arrow.either

import arrow.core.Either
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
object EitherAssertions {

  @TestFactory
  fun leftEither() = testFactoryFor(rootContext<Unit> {

    val anEither = Either.left("left")

    test("can assert on type") {
      expectThat(anEither).isLeft()
    }

    test("can assert on type and value equality") {
      expectThat(anEither).isLeft("left")
    }

    test("can assert on type with own predicate") {
      expectThat(anEither).isLeftWhere { it == "left" }
    }

    test("can chain assertion on narrowed type") {
      expectThat(anEither).isLeft()
        .get { a }
        .isEqualTo("left")
    }

    test("can have nested assertions on unwrapped type") {
      expectThat(Either.left(MyTuple("myName", 1, "uuid"))).expectLeft {
        get { name }.isEqualTo("myName")
        get { id }.isNotNull().isGreaterThan(0L)
        get { uuid }.isNotNull().isNotBlank()
      }
    }
  })

  @TestFactory
  fun rightEither() = testFactoryFor(rootContext<Unit> {

    val anEither = Either.right("right")

    test("can assert on type") {
      expectThat(anEither).isRight()
    }

    test("can assert on type and value equality") {
      expectThat(anEither).isRight("right")
    }

    test("can assert on type with own predicate") {
      expectThat(anEither).isRightWhere { it == "right" }
    }

    test("can chain assertion on narrowed type") {
      expectThat(anEither).isRight()
        .get { b }
        .isEqualTo("right")
    }

    test("can have nested assertions on unwrapped type") {
      expectThat(Either.right(MyTuple("myName", 1, "uuid"))).expectRight {
        get { name }.isEqualTo("myName")
        get { id }.isNotNull().isGreaterThan(0L)
        get { uuid }.isNotNull().isNotBlank()
      }
    }
  })
}

