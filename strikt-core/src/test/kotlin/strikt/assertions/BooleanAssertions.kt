package strikt.assertions

import com.oneeyedmen.minutest.junit.junitTests
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.fails

@DisplayName("assertions on Boolean")
@Suppress("SimplifyBooleanWithConstants")
internal object BooleanAssertions {
  @TestFactory
  fun isTrue() = junitTests<Unit> {
    test("passes when the subject is true") {
      expectThat("a" == "a").isTrue()
    }

    test("fails when the subject is false") {
      fails {
        expectThat("a" == "A").isTrue()
      }
    }

    test("fails when the subject is null") {
      fails {
        expectThat(null).isTrue()
      }
    }
  }

  @TestFactory
  fun isFalse() = junitTests<Unit> {
    test("passes when the subject is false") {
      expectThat("a" == "A").isFalse()
    }

    test("fails when the subject is false") {
      fails {
        expectThat("a" == "a").isFalse()
      }
    }

    test("fails when the subject is null") {
      fails {
        expectThat(null).isFalse()
      }
    }
  }
}
