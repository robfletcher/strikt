package strikt.spring

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.http.MediaType.IMAGE_PNG
import strikt.api.Assertion
import strikt.api.expectThat

internal object MediaTypeAssertions : JUnit5Minutests {

  fun tests() = rootContext<Assertion.Builder<MediaType?>> {

    context("a non-null media type subject") {
      fixture {
        expectThat(APPLICATION_JSON_UTF8)
      }

      context("isCompatibleWith assertion") {
        test("passes if the content type is an exact match") {
          isCompatibleWith(APPLICATION_JSON_UTF8)
        }

        test("passes if the content type is a sub-type") {
          isCompatibleWith(APPLICATION_JSON)
        }

        test("fails if the content type is different") {
          assertThrows<AssertionFailedError> {
            isCompatibleWith(IMAGE_PNG)
          }
        }
      }
    }

    context("a null media type subject") {
      fixture {
        expectThat(null)
      }

      test("is not compatible with anything") {
        assertThrows<AssertionFailedError> {
          isCompatibleWith(APPLICATION_JSON)
        }
      }
    }
  }
}
