package strikt.spring

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isNotNull
import strikt.spring.app.App
import strikt.spring.app.Controller

@ExtendWith(SpringExtension::class)
@SpringBootTest(
  classes = [App::class, Controller::class],
  webEnvironment = MOCK
)
@AutoConfigureMockMvc
internal class MockHttpServletResponseAssertions : JUnit5Minutests {
  @Autowired
  lateinit var mvc: MockMvc

  fun tests() = rootContext<MvcResult> {
    context("a request for XML") {
      fixture {
        mvc
          .perform(get("/").accept(APPLICATION_JSON))
          .andExpect(status().isOk)
          .andReturn()
      }

      context("content type mapping") {
        test("maps to the response content type") {
          expectThat(response)
            .contentType
            .isNotNull()
            .isA<MediaType>()
            .isCompatibleWith(APPLICATION_JSON)
        }
      }
    }
  }
}
