package strikt.arrow.either

import arrow.core.Either
import arrow.core.Predicate
import strikt.api.Assertion
import strikt.api.expectThat

@Suppress("UNCHECKED_CAST")
fun <L, R> Assertion.Builder<Either<L, R>>.isRight() =
    assert("should be Right") {
        when (it) {
            is Either.Right -> pass()
            is Either.Left -> fail()
        }
    } as Assertion.Builder<Either.Right<R>>

@Suppress("UNCHECKED_CAST")
fun <L, R> Assertion.Builder<Either<L, R>>.isRight(value: R) =
    assert("should be Right($value)") {
        when (it) {
            is Either.Right -> if (it.b == value) {
                pass()
            } else {
                fail()
            }

            else -> fail()
        }
    } as Assertion.Builder<Either.Right<R>>

@Suppress("UNCHECKED_CAST")
fun <L, R> Assertion.Builder<Either<L, R>>.isRightWhere(predicate: Predicate<R>) =
    assert("should be Right") {
        when (it) {
            is Either.Right -> if (predicate(it.b)) {
                pass()
            } else {
                fail()
            }

            else -> fail()
        }
    } as Assertion.Builder<Either.Right<R>>


@Suppress("UNCHECKED_CAST")
fun <L, R> Assertion.Builder<Either<L, R>>.expectRight( expect: Assertion.Builder<R>.() -> Unit) =
  assert("should be right") {
    when(it) {
      is Either.Left -> fail()
      is Either.Right -> expectThat(it.b).expect()
    }
  } as Assertion.Builder<Either.Right<R>>


@Suppress("UNCHECKED_CAST")
fun <L, R> Assertion.Builder<Either<L, R>>.isLeft() =
    assert("should be Left") {
        when {
            it.isRight() -> fail()
            it.isLeft() -> pass()
        }
    } as Assertion.Builder<Either.Left<L>>

@Suppress("UNCHECKED_CAST")
fun <L, R> Assertion.Builder<Either<L, R>>.isLeft(value: L) =
    assert("should be Left($value)") {
        when (it) {
            is Either.Left -> {
                if (it.a == value) {
                    pass()
                } else {
                    fail()
                }
            }
            else -> fail()
        }
    } as Assertion.Builder<Either.Left<L>>

@Suppress("UNCHECKED_CAST")
fun <L, R> Assertion.Builder<Either<L, R>>.isLeftWhere(predicate: Predicate<L>) =
    assert("should be Left") {
        when (it) {
            is Either.Left -> if (predicate(it.a)) {
                pass()
            } else {
                fail()
            }

            else -> fail()
        }
    } as Assertion.Builder<Either.Left<L>>

@Suppress("UNCHECKED_CAST")
fun <L, R> Assertion.Builder<Either<L, R>>.expectLeft( expect: Assertion.Builder<L>.() -> Unit) =
  assert("should be Left") {
    when(it) {
      is Either.Right -> fail()
      is Either.Left -> expectThat(it.a).expect()
    }
  } as Assertion.Builder<Either.Left<L>>
