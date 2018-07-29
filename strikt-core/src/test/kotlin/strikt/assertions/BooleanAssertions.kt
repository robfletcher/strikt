package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.fails

@DisplayName("assertions on Boolean")
@Suppress("SimplifyBooleanWithConstants")
internal class BooleanAssertions {
  @Nested
  @DisplayName("isTrue assertion")
  inner class IsTrue {
    @Test
    fun `passes when the subject is true`() {
      expect("a" == "a").isTrue()
    }

    @Test
    fun `fails when the subject is false`() {
      fails {
        expect("a" == "A").isTrue()
      }
    }

    @Test
    fun `fails when the subject is null`() {
      fails {
        expect(null).isTrue()
      }
    }
  }

  @Nested
  @DisplayName("isFalse assertion")
  inner class IsFalse {
    @Test
    fun `passes when the subject is false`() {
      expect("a" == "A").isFalse()
    }

    @Test
    fun `fails when the subject is false`() {
      fails {
        expect("a" == "a").isFalse()
      }
    }

    @Test
    fun `fails when the subject is null`() {
      fails {
        expect(null).isFalse()
      }
    }
  }
}
