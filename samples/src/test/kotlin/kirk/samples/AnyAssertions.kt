package kirk.samples

import kirk.api.Assertion
import kirk.api.AssertionFailed
import kirk.api.expect
import kirk.assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal object AnyAssertions {

  @Test
  fun isNull() {
    val subject: Any? = null
    expect(subject).isNull()
  }

  @Test
  fun isNullFails() {
    assertThrows<AssertionFailed> {
      val subject: Any? = "covfefe"
      expect(subject).isNull()
    }
  }

  @Test
  @Suppress("USELESS_IS_CHECK")
  fun isNullDowncasts() {
    val subject: Any? = null
    expect(subject)
      .also { assert(it is Assertion<Any?>) }
      .isNull()
      .also { assert(it is Assertion<Nothing>) }
  }

  @Test
  fun isNotNullFails() {
    assertThrows<AssertionFailed> {
      val subject: Any? = null
      expect(subject).isNotNull()
    }
  }

  @Test
  fun isNotNull() {
    val subject: Any? = "covfefe"
    expect(subject).isNotNull()
  }

  @Test
  @Suppress("USELESS_IS_CHECK")
  fun isNotNullDowncast() {
    val subject: Any? = "covfefe"
    expect(subject)
      .also { assert(it is Assertion<Any?>) }
      .isNotNull()
      .also { assert(it is Assertion<Any>) }
  }

  @Test
  fun isAOnNullSubject() {
    assertThrows<AssertionFailed> {
      val subject: Any? = null
      expect(subject).isA<String>()
    }
  }

  @Test
  fun isAOnWrongType() {
    assertThrows<AssertionFailed> {
      val subject = 1L
      expect(subject).isA<String>()
    }
  }

  @Test
  fun isAExactType() {
    val subject = "covfefe"
    expect(subject).isA<String>()
  }

  @Test
  fun isASubType() {
    val subject: Any = 1L
    expect(subject).isA<Number>()
  }

  @Test
  @Suppress("USELESS_IS_CHECK")
  fun isADowncast() {
    val subject: Any = 1L
    expect(subject)
      .also { assert(it is Assertion<Any>) }
      .isA<Number>()
      .also { assert(it is Assertion<Number>) }
      .isA<Long>()
      .also { assert(it is Assertion<Long>) }
  }

  @Test
  @Suppress("USELESS_IS_CHECK")
  fun isAChain() {
    val subject: Any = "covfefe"
    expect(subject)
      .also { assert(it is Assertion<Any>) }
      .isA<String>()
      .also { assert(it is Assertion<String>) }
      .hasLength(7) // only available on Assertion<CharSequence>
  }

  @Test
  fun isEqualTo() {
    expect("covfefe").isEqualTo("covfefe")
  }

  @Test
  fun isEqualToFails() {
    assertThrows<AssertionFailed> {
      expect("covfefe").isEqualTo("COVFEFE")
    }
  }

  @Test
  fun isEqualToDifferentType() {
    assertThrows<AssertionFailed> {
      expect(1).isEqualTo(1L)
    }
  }

  @Test
  fun isEqualToNullSubject() {
    assertThrows<AssertionFailed> {
      expect(null).isEqualTo("covfefe")
    }
  }

  @Test
  fun isEqualToNullExpected() {
    assertThrows<AssertionFailed> {
      expect("covfefe").isEqualTo(null)
    }
  }

  @Test
  fun isNotEqualToFails() {
    assertThrows<AssertionFailed> {
      expect("covfefe").isNotEqualTo("covfefe")
    }
  }

  @Test
  fun isNotEqualTo() {
    expect("covfefe").isNotEqualTo("COVFEFE")
  }

  @Test
  fun isNotEqualToDifferentType() {
    expect(1).isNotEqualTo(1L)
  }

  @Test
  fun isNotEqualToNullSubject() {
    expect(null).isNotEqualTo("covfefe")
  }

  @Test
  fun isNotEqualToNullExpected() {
    expect("covfefe").isNotEqualTo(null)
  }
}
