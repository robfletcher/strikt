package strikt.java

import strikt.api.Assertion
import java.util.Optional

/**
 * Asserts that an `Optional` contains a value (is not empty) and returns an
 * assertion builder whose subject is the value.
 */
fun <T> Assertion.Builder<Optional<T>>.isPresent(): Assertion.Builder<T> =
  assertThat("a value is present", Optional<T>::isPresent)
    .get(Optional<T>::get)

/**
 * Asserts that an `Optional` does not contain a value.
 */
fun <T> Assertion.Builder<Optional<T>>.isAbsent(): Assertion.Builder<Optional<T>> =
  assertThat("no value is present") {
    !it.isPresent
  }

/**
 * Maps an assertion on a Java `Optional` to a Kotlin nullable type.
 */
fun <T> Assertion.Builder<Optional<T>>.toNullable(): Assertion.Builder<T?> =
  get { orElse(null) }
