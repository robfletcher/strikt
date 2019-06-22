package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.api.tryCatching

@DisplayName("assertions")
internal object Assertions {

  @TestFactory
  fun suppression() = assertionTests<Any?> {
    context("failing") {
      fixture { expectThat(false) }

      test("exceptions are suppressed") {
        val result = tryCatching {
          isEqualTo(true)
        }
        expectThat(result)
          .failed()
          .exception
          .get { stackTrace.toList() }
          .isNotEmpty()
          .map { it.className }
          .none {
            startsWith("strikt")
          }
        expectThat(result)
          .failed()
          .exception
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
