package strikt.spring

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.opentest4j.AssertionFailedError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.http.MediaType.IMAGE_PNG
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isNotNull
import strikt.spring.app.App
import strikt.spring.app.Controller
import javax.servlet.ServletResponse

@ExtendWith(SpringExtension::class)
@SpringBootTest(
  classes = [App::class, Controller::class],
  webEnvironment = MOCK
)
@AutoConfigureMockMvc
internal class ServletResponseAssertions : JUnit5Minutests {
  @Autowired
  lateinit var mvc: MockMvc

  fun tests() = rootContext<Assertion.Builder<ServletResponse>> {
    context("a request for JSON") {
      fixture {
        mvc
          .perform(get("/").accept(APPLICATION_JSON))
          .andExpect(status().isOk)
          .andReturn()
          .response
          .run { expectThat(this) }
      }

      context("contentType mapping") {
        test("maps to the response content type") {
          contentType
            .isNotNull()
            .isA<MediaType>()
            .isCompatibleWith(APPLICATION_JSON)
        }
      }

      context("contentTypeIsCompatibleWith assertion") {
        test("passes if the content type is an exact match") {
          contentTypeIsCompatibleWith(APPLICATION_JSON_UTF8)
        }

        test("passes if the content type is a sub-type") {
          contentTypeIsCompatibleWith(APPLICATION_JSON)
        }

        test("fails if the content type is not compatible") {
          assertThrows<AssertionFailedError> {
            contentTypeIsCompatibleWith(IMAGE_PNG)
          }
        }
      }
    }
  }
}
