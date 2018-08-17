package strikt.assertions

import strikt.api.Assertion

fun <T> Assertion.Builder<Array<out T>>.contentEquals(other: Array<out T>): Assertion.Builder<Array<out T>> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

fun Assertion.Builder<BooleanArray>.contentEquals(other: BooleanArray): Assertion.Builder<BooleanArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

fun Assertion.Builder<ByteArray>.contentEquals(other: ByteArray): Assertion.Builder<ByteArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

fun Assertion.Builder<ShortArray>.contentEquals(other: ShortArray): Assertion.Builder<ShortArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

fun Assertion.Builder<IntArray>.contentEquals(other: IntArray): Assertion.Builder<IntArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

fun Assertion.Builder<LongArray>.contentEquals(other: LongArray): Assertion.Builder<LongArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

fun Assertion.Builder<FloatArray>.contentEquals(other: FloatArray): Assertion.Builder<FloatArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

fun Assertion.Builder<DoubleArray>.contentEquals(other: DoubleArray): Assertion.Builder<DoubleArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }

fun Assertion.Builder<CharArray>.contentEquals(other: CharArray): Assertion.Builder<CharArray> =
  passesIf("array content equals %s", other) {
    it.contentEquals(other)
  }
