package strikt.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType.MISSING
import com.fasterxml.jackson.databind.node.JsonNodeType.NULL
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isNull
import strikt.assertions.isTrue

internal object JsonNodeAssertions : JUnit5Minutests {

  fun tests() = rootContext<JsonNode> {
    fixture {
      jacksonObjectMapper().readTree(
        """
        {
          "name": "Joshua Abraham Norton",
          "titles": [
            "Emperor of the United States",
            "Protector of Mexico"
          ],
          "year-of-birth": 1818,
          "deceased": true,
          "place-of-birth": null
        }
      """
      )
    }

    context("has assertion") {
      test("passes if the node has the specified field") {
        expectThat(this).has("name")
      }

      test("fails if the node does not have the specified field") {
        assertThrows<AssertionFailedError> {
          expectThat(this).has("date-of-birth")
        }
      }
    }

    context("path mapping") {
      test("maps the assertion to the specified field") {
        expectThat(this)
          .path("name")
          .get { textValue() }
          .isEqualTo("Joshua Abraham Norton")
      }

      test("mapping to a field that does not exist results in a missing node") {
        expectThat(this)
          .path("date-of-birth")
          .get { nodeType }
          .isEqualTo(MISSING)
      }
    }

    context("isObject assertion") {
      test("passes if the node is an object") {
        expectThat(this)
          .isObject()
      }

      test("fails if the node is not an object") {
        assertThrows<AssertionFailedError> {
          expectThat(this)
            .path("name")
            .isObject()
        }
      }
    }

    context("isArray assertion") {
      test("passes if the node is an array") {
        expectThat(this)
          .path("titles")
          .isArray()
      }

      test("fails if the node is not an array") {
        assertThrows<AssertionFailedError> {
          expectThat(this)
            .isArray()
        }
      }
    }

    context("isTextual assertion") {
      test("passes if the node is a text node") {
        expectThat(this)
          .path("name")
          .isTextual()
      }

      test("fails if the node is not a text node") {
        assertThrows<AssertionFailedError> {
          expectThat(this)
            .isTextual()
        }
      }
    }

    context("isNumber assertion") {
      test("passes if the node is a numeric node") {
        expectThat(this)
          .path("year-of-birth")
          .isNumber()
      }

      test("fails if the node is not a numeric node") {
        assertThrows<AssertionFailedError> {
          expectThat(this)
            .isNumber()
        }
      }
    }

    context("isBoolean assertion") {
      test("passes if the node is a boolean node") {
        expectThat(this)
          .path("deceased")
          .isBoolean()
      }

      test("fails if the node is not a boolean node") {
        assertThrows<AssertionFailedError> {
          expectThat(this)
            .isBoolean()
        }
      }
    }

    context("isMissing assertion") {
      test("passes if the node is a missing node") {
        expectThat(this)
          .path("date-of-birth")
          .isMissing()
      }

      test("fails if the node is not a missing node") {
        assertThrows<AssertionFailedError> {
          expectThat(this)
            .isMissing()
        }
      }
    }

    context("hasNodeType assertion") {
      test("passes if the node is the specified type") {
        expectThat(this)
          .path("place-of-birth")
          .hasNodeType(NULL)
      }

      test("fails if the node is a different type") {
        assertThrows<AssertionFailedError> {
          expectThat(this)
            .hasNodeType(NULL)
        }
      }
    }

    context("textValue mapping") {
      test("maps a text node to its text value") {
        expectThat(this)
          .path("name")
          .textValue()
          .isEqualTo("Joshua Abraham Norton")
      }

      test("maps any other node type to null") {
        expectThat(this)
          .path("titles")
          .textValue()
          .isNull()
      }
    }

    context("numberValue mapping") {
      test("maps a numeric node to its number value") {
        expectThat(this)
          .path("year-of-birth")
          .numberValue()
          .isEqualTo(1818)
          .isA<Int>()
      }

      test("maps any other node type to null") {
        expectThat(this)
          .path("name")
          .numberValue()
          .isNull()
      }
    }

    context("booleanValue mapping") {
      test("maps a boolean node to its boolean value") {
        expectThat(this)
          .path("deceased")
          .booleanValue()
          .isTrue()
      }

      test("maps any other node type to false") {
        expectThat(this)
          .path("name")
          .booleanValue()
          .isFalse()
      }
    }
  }
}
