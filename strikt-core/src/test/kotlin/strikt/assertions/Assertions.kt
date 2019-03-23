package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.catching
import strikt.api.expectThat
import strikt.api.expectThrows

@DisplayName("assertions")
internal object Assertions {

  @TestFactory
  fun suppression() = assertionTests<Any?> {
    context("failing") {
      fixture { expectThat(false) }

      test("exceptions are suppressed") {
        val error = catching {
          isEqualTo(true)
        }
        expectThat(error!!.stackTrace.none { it.className.startsWith("strikt") })
        val suppressed = error.suppressed
        expectThat(suppressed.size).isEqualTo(1)
        expectThat(suppressed.single().stackTrace.all { it.methodName.startsWith("strict") })
      }
    }
  }
}
