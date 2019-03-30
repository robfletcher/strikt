package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import strikt.api.catching
import strikt.api.expectThat

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
        expectThat(error)
          .isNotNull()
          .get { stackTrace.toList() }
          .isNotEmpty()
          .map { it.className }
          .none {
            startsWith("strikt")
          }
        expectThat(error)
          .isNotNull()
          .get { suppressed.toList() }
          .hasSize(1)
          .single()
          .get { stackTrace.toList() }
          .isNotEmpty()
          .map { it.className }
          .any {
            startsWith("strikt")
          }
      }
    }
  }
}
