package strikt.spring

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import org.springframework.http.HttpStatus.CONTINUE
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.MOVED_PERMANENTLY
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.spring.app.Album

internal class ResponseEntityAssertions : JUnit5Minutests {
  private val album = Album("Almost Killed Me", 2004)

  fun tests() = rootContext<Assertion.Builder<ResponseEntity<Album>>> {

    context("statusCodeIs1xxInformational assertion") {
      context("passes") {
        fixture {
          expectThat(ResponseEntity.status(CONTINUE).body(album))
        }

        test("if status code is 1xx") {
          statusCodeIs1xxInformational()
        }
      }
      context("fails") {
        fixture {
          expectThat(ResponseEntity.ok(album))
        }

        test("if status code is not 1xx") {
          assertThrows<AssertionFailedError> {
            statusCodeIs1xxInformational()
          }
        }
      }
    }

    context("statusCodeIs2xxSuccess assertion") {
      context("passes") {
        fixture {
          expectThat(ResponseEntity.ok(album))
        }

        test("if status code is 2xx") {
          statusCodeIs2xxSuccess()
        }
      }
      context("fails") {
        fixture {
          expectThat(ResponseEntity.status(INTERNAL_SERVER_ERROR).body(album))
        }

        test("if status code is not 2xx") {
          assertThrows<AssertionFailedError> {
            statusCodeIs2xxSuccess()
          }
        }
      }
    }

    context("statusCodeIs3xxRedirect assertion") {
      context("passes") {
        fixture {
          expectThat(ResponseEntity.status(MOVED_PERMANENTLY).body(album))
        }

        test("if status code is 3xx") {
          statusCodeIs3xxRedirection()
        }
      }
      context("fails") {
        fixture {
          expectThat(ResponseEntity.status(INTERNAL_SERVER_ERROR).body(album))
        }

        test("if status code is not 3xx") {
          assertThrows<AssertionFailedError> {
            statusCodeIs3xxRedirection()
          }
        }
      }
    }

    context("statusCodeIs4xxClientError assertion") {
      context("passes") {
        fixture {
          expectThat(ResponseEntity.unprocessableEntity().body(album))
        }

        test("if status code is 4xx") {
          statusCodeIs4xxClientError()
        }
      }
      context("fails") {
        fixture {
          expectThat(ResponseEntity.status(INTERNAL_SERVER_ERROR).body(album))
        }

        test("if status code is not 4xx") {
          assertThrows<AssertionFailedError> {
            statusCodeIs4xxClientError()
          }
        }
      }
    }

    context("statusCodeIs5xxServerError assertion") {
      context("passes") {
        fixture {
          expectThat(ResponseEntity.status(INTERNAL_SERVER_ERROR).body(album))
        }

        test("if status code is 5xx") {
          statusCodeIs5xxServerError()
        }
      }
      context("fails") {
        fixture {
          expectThat(ResponseEntity.ok(album))
        }

        test("if status code is not 5xx") {
          assertThrows<AssertionFailedError> {
            statusCodeIs5xxServerError()
          }
        }
      }
    }

    context("statusCodeIs assertion") {
      fixture {
        expectThat(ResponseEntity.ok(album))
      }

      test("passes if the status code is 200") {
        statusCodeIs(200)
      }

      test("fails if the status code is not 200") {
        assertThrows<AssertionFailedError> {
          statusCodeIs(500)
        }
      }
    }

    context("statusCodeIs assertion") {
      fixture {
        expectThat(ResponseEntity.ok(album))
      }

      test("passes if the status code is 200") {
        statusCodeIs(OK)
      }

      test("fails if the status code is not 200") {
        assertThrows<AssertionFailedError> {
          statusCodeIs(INTERNAL_SERVER_ERROR)
        }
      }
    }
  }
}
