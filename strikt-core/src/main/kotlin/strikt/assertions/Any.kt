package strikt.assertions

import strikt.api.Assertion.Builder
import kotlin.reflect.full.memberProperties

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
      is T -> pass()
      else -> fail(actual = it?.javaClass)
    }
  } as Builder<T>

/**
 * Asserts that the subject is equal to [expected] according to the standard
 * Kotlin `==` operator.
 *
 * @param expected the expected value.
 */
fun <T> Builder<T>.isEqualTo(expected: T?): Builder<T> =
  assert("is equal to %s", expected) {
    when (it) {
      expected -> pass()
      else -> fail(actual = it)
    }
  }

/**
 * Asserts that the subject is not equal to [expected] according to the standard
 * Kotlin `==` operator.
 *
 * @param expected the expected value.
 */
fun <T> Builder<T>.isNotEqualTo(expected: T?): Builder<T> =
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
fun <T> Builder<T>.isSameInstanceAs(expected: Any?): Builder<T> =
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
fun <T> Builder<T>.isNotSameInstanceAs(expected: Any?): Builder<T> =
  assert("is not the same instance as %s", expected) {
    when {
      it === expected -> fail()
      else -> pass()
    }
  }

fun <T : Any> Builder<T>.allPropertiesAreEqualTo(other: T): Builder<T> =
  compose("is equal field-by-field to %s", other) {
    it.javaClass.kotlin.memberProperties.forEach { property ->
      when {
        property.returnType.javaClass.isArray ->
          map(property).isA<Array<*>>().contentEquals(other as Array<*>)
        else ->
          map(property).isEqualTo(property.get(other))
      }
    }
  } then {
    if (allPassed) pass() else fail()
  }
