package kirk.api

import kirk.internal.Mode

/**
 * Allows reporting of success or failure by assertion implementations.
 *
 * This class is the receiver of the lambda passed to [Assertion.assert].
 *
 * @property parent The assertion subject.
 * @see Assertion.assert
 */
class AssertionContext<T>
internal constructor(
  description: String,
  private val parent: Subject<T>,
  private val mode: Mode
) {

  private var result = Result(description).also(parent::append)

  val subject: T = parent.value

  /**
   * Report that the assertion succeeded.
   */
  fun pass() {
    result.pass()
  }

  /**
   * Report that the assertion failed.
   */
  fun fail() {
    result.fail()
    if (mode == Mode.FAIL_FAST) {
      throw AssertionFailed(parent.root)
    }
  }

  /**
   * Report that the assertion failed.
   *
   * @param actualDescription descriptive text about [actualValue] including a
   * placeholder in [String.format] notation for [actualValue].
   * @param actualValue the value(s) that violated the assertion.
   */
  fun fail(actualDescription: String, actualValue: Any?) {
    result.fail(actualDescription, actualValue)
    if (mode == Mode.FAIL_FAST) {
      throw AssertionFailed(parent.root)
    }
  }

  /**
   * Allows an assertion to be composed of multiple sub-assertions such as on
   * fields of an object or elements of a collection.
   *
   * The results of assertions made inside the [assertions] block are included
   * under the overall assertion result.
   *
   * @return the results of assertions made inside the [assertions] block.
   */
  fun compose(assertions: ComposedAssertions<T>.() -> Unit): ComposedAssertionResults {
    ComposedAssertions(parent, result).apply(assertions)
    return object : ComposedAssertionResults {
      override fun pass() {
        this@AssertionContext.pass()
      }

      override fun fail() {
        this@AssertionContext.fail()
      }

      override val allPassed: Boolean = result.allPassed
      override val anyPassed: Boolean = result.anyPassed
      override val allFailed: Boolean = result.allFailed
      override val anyFailed: Boolean = result.anyFailed
    }
  }
}

interface ComposedAssertionResults {
  fun pass()
  fun fail()
  val allPassed: Boolean
  val anyPassed: Boolean
  val allFailed: Boolean
  val anyFailed: Boolean

  /**
   * A convenient way to handle to composed assertion results.
   *
   * @see AssertionContext.compose
   */
  infix fun then(block: ComposedAssertionResults.() -> Unit) {
    this.block()
  }
}

