package strikt.samples

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.MultipleFailuresError
import strikt.api.Assertion
import strikt.api.expect
import strikt.assertions.*

internal object AnyAssertions {

  @Test
  fun isNull() {
    val subject: Any? = null
    expect(subject).isNull()
  }

  @Test
  fun isNullFails() {
    assertThrows<MultipleFailuresError> {
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
    assertThrows<MultipleFailuresError> {
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
    assertThrows<MultipleFailuresError> {
      val subject: Any? = null
      expect(subject).isA<String>()
    }
  }

  @Test
  fun isAOnWrongType() {
    assertThrows<MultipleFailuresError> {
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
    assertThrows<MultipleFailuresError> {
      expect("covfefe").isEqualTo("COVFEFE")
    }
  }

  @Test
  fun isEqualToDifferentType() {
    assertThrows<MultipleFailuresError> {
      expect(1).isEqualTo(1L)
    }
  }

  @Test
  fun isEqualToNullSubject() {
    assertThrows<MultipleFailuresError> {
      expect(null).isEqualTo("covfefe")
    }
  }

  @Test
  fun isEqualToNullExpected() {
    assertThrows<MultipleFailuresError> {
      expect("covfefe").isEqualTo(null)
    }
  }

  @Test
  fun isNotEqualToFails() {
    assertThrows<MultipleFailuresError> {
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
