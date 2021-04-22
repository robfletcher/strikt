package strikt.arrow

import arrow.core.Either
import strikt.api.Assertion

/**
 * Asserts that the [Either] is [Either.Right]
 * @return Assertion builder over the same subject that is now known to be
 * a [Either.Right].
 */
@Suppress("UNCHECKED_CAST")
fun <L, R> Assertion.Builder<Either<L, R>>.isRight() =
  assert("should be Right") { subject ->
    when (subject) {
      is Either.Right -> pass()
      is Either.Left -> fail()
    }
  } as Assertion.Builder<Either.Right<R>>

/**
 * Asserts that the [Either] is [Either.Right] and that it contains the exact value
 * @param value Value to compare to the [Either]'s wrapped value
 * @return Assertion builder over the same subject that is now known to be
 * a [Either.Right].
 */
@Suppress("UNCHECKED_CAST")
infix fun <L, R> Assertion.Builder<Either<L, R>>.isRight(value: R) =
  assert("should be Right($value)") { subject ->
    when (subject) {
      is Either.Right ->
        if (subject.value == value) {
          pass()
        } else {
          fail()
        }

      else -> fail()
    }
  } as Assertion.Builder<Either.Right<R>>

/**
 * Unwraps the containing value of the [Either.Right]
 * @return Assertion builder over the unwrapped subject
 */
@Deprecated("Use value instead", replaceWith = ReplaceWith("value"))
val <R> Assertion.Builder<Either.Right<R>>.b: Assertion.Builder<R>
  get() = value

/**
 * Unwraps the containing value of the [Either.Right]
 * @return Assertion builder over the unwrapped subject
 * @see Either.Right.value
 */
val <R> Assertion.Builder<Either.Right<R>>.value: Assertion.Builder<R>
  @JvmName("EitherRightValue")
  get() = get("right value", Either.Right<R>::value)

/**
 * Asserts that the [Either] is [Either.Left]
 * @return Assertion builder over the same subject that is now known to be
 * a [Either.Left].
 */
@Suppress("UNCHECKED_CAST")
fun <L, R> Assertion.Builder<Either<L, R>>.isLeft() =
  assert("should be Left") { subject ->
    when {
      subject.isRight() -> fail()
      subject.isLeft() -> pass()
    }
  } as Assertion.Builder<Either.Left<L>>

/**
 * Asserts that the [Either] is [Either.Left] and that it contains the exact value
 * @param value Value to compare to the [Either]'s wrapped value
 * @return Assertion builder over the same subject that is now known to be
 * a [Either.Left].
 */
@Suppress("UNCHECKED_CAST")
infix fun <L, R> Assertion.Builder<Either<L, R>>.isLeft(value: L) =
  assert("should be Left($value)") { subject ->
    when (subject) {
      is Either.Left -> {
        if (subject.value == value) {
          pass()
        } else {
          fail()
        }
      }
      else -> fail()
    }
  } as Assertion.Builder<Either.Left<L>>

/**
 * Unwraps the containing value of the [Either.Left]
 * @return Assertion builder over the unwrapped subject
 * @see Either.Left.value
 */
@Deprecated("Use value instead", replaceWith = ReplaceWith("value"))
val <L> Assertion.Builder<Either.Left<L>>.a: Assertion.Builder<L>
  get() = value

/**
 * Unwraps the containing value of the [Either.Left]
 * @return Assertion builder over the unwrapped subject
 */
val <L> Assertion.Builder<Either.Left<L>>.value: Assertion.Builder<L>
  @JvmName("EitherLeftValue")
  get() = get("left value", Either.Left<L>::value)
