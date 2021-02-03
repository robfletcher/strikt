package strikt.assertions

import strikt.api.Assertion.Builder

/**
 * Asserts that the subject is `null`.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Builder<T?>.isNull(): Builder<Nothing> =
  assert("is null", null) {
    when (it) {
      null -> pass()
      else -> fail()
    }
  } as Builder<Nothing>

/**
 * Asserts that the subject is not `null`.
 *
 * @return an assertion for a non-`null` subject.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Builder<T?>.isNotNull(): Builder<T> =
  assert("is not null") {
    when (it) {
      null -> fail()
      else -> pass()
    }
  } as Builder<T>

/**
 * Asserts that the subject is an instance of [T].
 *
 * @return an assertion for [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Builder<*>.isA(): Builder<T> =
  assert("is an instance of %s", T::class.java) {
    when (it) {
      null -> fail(actual = null)
      is T -> pass(actual = T::class.java)
      else -> fail(actual = it.javaClass)
    }
  } as Builder<T>

/**
 * Asserts that the subject is equal to [expected] according to the standard
 * Kotlin `==` operator.
 *
 * @param expected the expected value.
 */
infix fun <T> Builder<T>.isEqualTo(expected: T?): Builder<T> =
  assert("is equal to %s", expected) {
    when (it) {
      expected -> pass(actual = it)
      is ByteArray -> if (expected is ByteArray && it.contentEquals(expected)) pass(actual = it) else fail(actual = it)
      is BooleanArray -> if (expected is BooleanArray && it.contentEquals(expected)) pass(actual = it) else fail(actual = it)
      is CharArray -> if (expected is CharArray && it.contentEquals(expected)) pass(actual = it) else fail(actual = it)
      is ShortArray -> if (expected is ShortArray && it.contentEquals(expected)) pass(actual = it) else fail(actual = it)
      is IntArray -> if (expected is IntArray && it.contentEquals(expected)) pass(actual = it) else fail(actual = it)
      is LongArray -> if (expected is LongArray && it.contentEquals(expected)) pass(actual = it) else fail(actual = it)
      is FloatArray -> if (expected is FloatArray && it.contentEquals(expected)) pass(actual = it) else fail(actual = it)
      is DoubleArray -> if (expected is DoubleArray && it.contentEquals(expected)) pass(actual = it) else fail(actual = it)
      is Array<*> -> if (expected is Array<*> && it.contentEquals(expected)) pass(actual = it) else fail(actual = it)
      else -> fail(actual = it)
    }
  }

/**
 * Asserts that the subject is not equal to [expected] according to the standard
 * Kotlin `==` operator.
 *
 * @param expected the expected value.
 */
infix fun <T> Builder<T>.isNotEqualTo(expected: T?): Builder<T> =
  assert("is not equal to %s", expected) {
    when (it) {
      expected -> fail()
      else -> pass()
    }
  }

/**
 * Asserts that the subject is the same instance as [expected] according to the standard
 * Kotlin `===` operator.
 *
 * @param expected the expected instance.
 */
infix fun <T> Builder<T>.isSameInstanceAs(expected: Any?): Builder<T> =
  assert("is the same instance as %s", expected) {
    when {
      it === expected -> pass()
      else -> fail(actual = it)
    }
  }

/**
 * Asserts that the subject is not the same instance as [expected] according to the standard
 * Kotlin `===` operator.
 *
 * @param expected the expected instance.
 */
infix fun <T> Builder<T>.isNotSameInstanceAs(expected: Any?): Builder<T> =
  assert("is not the same instance as %s", expected) {
    when {
      it === expected -> fail()
      else -> pass()
    }
  }
