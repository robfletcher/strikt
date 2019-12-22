package strikt.assertions

import kotlin.math.absoluteValue
import strikt.api.Assertion.Builder

/**
 * Asserts that the subject is within [tolerance] of [expected].
 */
fun Builder<Double>.isEqualTo(expected: Double, tolerance: Double) =
  assert("is within $tolerance of $expected") {
    val diff = it - expected
    if (diff.absoluteValue < tolerance)
      pass()
    else
      fail(actual = it, description = "differs by $diff")
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
      fail(actual = it, description = "differs by $diff")
  }
