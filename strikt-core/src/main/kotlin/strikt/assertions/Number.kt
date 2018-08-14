package strikt.assertions

import strikt.api.Assertion.Builder
import kotlin.math.absoluteValue

/**
 * Asserts that the subject is within [tolerance] of [expected].
 */
fun Builder<Double>.isEqualTo(expected: Double, tolerance: Double) =
  assert("is within $tolerance of $expected") {
    val diff = it - expected
    if (diff.absoluteValue < tolerance)
      pass()
    else
      fail(actual = diff, description = "differs by %f")
  }

/**
 * Asserts that the subject is within [tolerance] of [expected].
 */
fun Builder<Float>.isEqualTo(expected: Float, tolerance: Double) =
  assert("is within $tolerance of $expected") {
    val diff = it - expected
    if (diff.absoluteValue < tolerance)
      pass()
    else
      fail(actual = diff, description = "differs by %f")
  }
