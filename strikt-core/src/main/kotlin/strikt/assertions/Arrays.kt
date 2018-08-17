package strikt.assertions

import strikt.api.Assertion

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [Array.contentEquals].
 */
fun <T> Assertion.Builder<Array<out T>>.contentEquals(other: Array<out T>): Assertion.Builder<Array<out T>> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [BooleanArray.contentEquals].
 */
fun Assertion.Builder<BooleanArray>.contentEquals(other: BooleanArray): Assertion.Builder<BooleanArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [ByteArray.contentEquals].
 */
fun Assertion.Builder<ByteArray>.contentEquals(other: ByteArray): Assertion.Builder<ByteArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [ShortArray.contentEquals].
 */
fun Assertion.Builder<ShortArray>.contentEquals(other: ShortArray): Assertion.Builder<ShortArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [IntArray.contentEquals].
 */
fun Assertion.Builder<IntArray>.contentEquals(other: IntArray): Assertion.Builder<IntArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [LongArray.contentEquals].
 */
fun Assertion.Builder<LongArray>.contentEquals(other: LongArray): Assertion.Builder<LongArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [FloatArray.contentEquals].
 */
fun Assertion.Builder<FloatArray>.contentEquals(other: FloatArray): Assertion.Builder<FloatArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [DoubleArray.contentEquals].
 */
fun Assertion.Builder<DoubleArray>.contentEquals(other: DoubleArray): Assertion.Builder<DoubleArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [CharArray.contentEquals].
 */
fun Assertion.Builder<CharArray>.contentEquals(other: CharArray): Assertion.Builder<CharArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }
