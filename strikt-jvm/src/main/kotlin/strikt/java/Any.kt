package strikt.java

import strikt.api.Assertion
import strikt.assertions.contentEquals
import strikt.assertions.isEqualTo
import java.beans.Introspector
import kotlin.reflect.KProperty1

/**
 * Asserts that all properties of the subject match those of [other] according
 * to either [contentEquals] in the case of array properties or [isEqualTo] in
 * other cases.
 *
 * Properties are identified using Java beans conventions.
 */
infix fun <T : Any> Assertion.Builder<T>.propertiesAreEqualTo(other: T): Assertion.Builder<T> = compareFieldByField(other)

/**
 * Asserts that all properties of the subject except [ignoredProperties] match
 * those of [other] according to either [contentEquals] in the case of array
 * properties or [isEqualTo] in other cases.
 *
 * Properties are identified using Java beans conventions.
 */
fun <T : Any> Assertion.Builder<T>.propertiesAreEqualToIgnoring(
  other: T,
  vararg ignoredProperties: KProperty1<T, Any>
): Assertion.Builder<T> =
  compareFieldByField(
    other,
    ignoredProperties.map(KProperty1<T, Any>::name)
  )

private fun <T : Any> Assertion.Builder<T>.compareFieldByField(
  other: T,
  ignoredPropertyNames: List<String> = emptyList()
): Assertion.Builder<T> =
  compose("is equal field-by-field to %s", other) { subject ->
    Introspector.getBeanInfo(subject.javaClass).let { beanInfo ->
      beanInfo
        .propertyDescriptors
        .filter { it.name != "class" && it.name !in ignoredPropertyNames }
        .forEach { property ->
          val mappedAssertion =
            get("value of property ${property.name}") {
              property.readMethod(this)
            }
          val otherValue = property.readMethod(other)
          @Suppress("UNCHECKED_CAST")
          when {
            property.propertyType == BooleanArray::class.java ->
              (mappedAssertion as Assertion.Builder<BooleanArray>)
                .contentEquals(otherValue as BooleanArray)
            property.propertyType == ByteArray::class.java ->
              (mappedAssertion as Assertion.Builder<ByteArray>)
                .contentEquals(otherValue as ByteArray)
            property.propertyType == ShortArray::class.java ->
              (mappedAssertion as Assertion.Builder<ShortArray>)
                .contentEquals(otherValue as ShortArray)
            property.propertyType == IntArray::class.java ->
              (mappedAssertion as Assertion.Builder<IntArray>)
                .contentEquals(otherValue as IntArray)
            property.propertyType == LongArray::class.java ->
              (mappedAssertion as Assertion.Builder<LongArray>)
                .contentEquals(otherValue as LongArray)
            property.propertyType == FloatArray::class.java ->
              (mappedAssertion as Assertion.Builder<FloatArray>)
                .contentEquals(otherValue as FloatArray)
            property.propertyType == DoubleArray::class.java ->
              (mappedAssertion as Assertion.Builder<DoubleArray>)
                .contentEquals(otherValue as DoubleArray)
            property.propertyType.isArray ->
              (mappedAssertion as Assertion.Builder<Array<*>>)
                .contentEquals(otherValue as Array<*>)
            else ->
              mappedAssertion.isEqualTo(otherValue)
          }
        }
    }
  } then {
    if (allPassed) pass() else fail()
  }
