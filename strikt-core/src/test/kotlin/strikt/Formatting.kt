package strikt

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.all
import strikt.assertions.contains
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isUpperCase
import strikt.assertions.startsWith
import java.time.LocalDate

@DisplayName("error message formatting")
internal class Formatting {
  @Test
  fun `a failing chained assertion formats the message correctly`() {
    val error =
      assertThrows<AssertionError> {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expectThat(subject)
          .describedAs("a couple of words")
          .hasSize(3)
          .all { isUpperCase() }
          .all { startsWith('c') }
      }

    val expected =
      """▼ Expect that a couple of words:
        |  ✓ has size 3
        |  ✗ all elements match:
        |    ▼ "catflap":
        |      ✗ is upper case
        |    ▼ "rubberplant":
        |      ✗ is upper case
        |    ▼ "marzipan":
        |      ✗ is upper case
      """.trimMargin()
    expectThat(error.message).isEqualTo(expected)
  }

  @Test
  fun `a failing block assertion formats the message correctly`() {
    val error =
      assertThrows<AssertionError> {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expectThat(subject) {
          hasSize(0)
          all {
            isUpperCase()
            startsWith('c')
          }
        }
      }

    val expected =
      """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
        |  ✗ has size 0
        |       found 3
        |  ✗ all elements match:
        |    ▼ "catflap":
        |      ✗ is upper case
        |      ✓ starts with 'c'
        |    ▼ "rubberplant":
        |      ✗ is upper case
        |      ✗ starts with 'c'
        |              found 'r'
        |    ▼ "marzipan":
        |      ✗ is upper case
        |      ✗ starts with 'c'
        |              found 'm'
      """.trimMargin()
    expectThat(error.message).isEqualTo(expected)
  }

  @Test
  fun `passing assertions are included in the error message`() {
    val error =
      assertThrows<AssertionError> {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expectThat(subject) {
          hasSize(3)
          all {
            startsWith('c')
          }
        }
      }

    val expected =
      """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
        |  ✓ has size 3
        |  ✗ all elements match:
        |    ▼ "catflap":
        |      ✓ starts with 'c'
        |    ▼ "rubberplant":
        |      ✗ starts with 'c'
        |              found 'r'
        |    ▼ "marzipan":
        |      ✗ starts with 'c'
        |              found 'm'
      """.trimMargin()

    expectThat(error.message).isEqualTo(expected)
  }

  @Test
  fun `an own toString is preferred to mapping over iterable`() {
    val toStringOutput = "useful toString info"
    val iteratorOutput = "less useful iterator output"

    val subject =
      object : Iterable<String> {
        override fun iterator(): Iterator<String> = listOf(iteratorOutput).iterator()

        override fun toString(): String = toStringOutput
      }

    val e =
      assertThrows<AssertionError> {
        expectThat(subject) { isNotEqualTo(subject) }
      }

    expectThat(e.message).isNotNull().and {
      contains(toStringOutput)
      not().contains(iteratorOutput)
    }
  }

  @Test
  fun `iterable is used when there is no own toString method`() {
    val iteratorOutput = "useful iterable info"

    val subject =
      object : Iterable<String> {
        override fun iterator(): Iterator<String> = listOf(iteratorOutput).iterator()
      }

    val e =
      assertThrows<AssertionError> {
        expectThat(subject) { isNotEqualTo(subject) }
      }

    expectThat(e.message).isNotNull().contains(iteratorOutput)
  }

  @Test
  fun `multi-line string values in failure messages are formatted with margins`() {
    val subject =
      """a string
               |with
               |line breaks
      """.trimMargin()

    val e =
      assertThrows<AssertionError> {
        expectThat(subject) isEqualTo
          """a different string
                                      |with
                                      |line breaks
          """.trimMargin()
      }

    expectThat(e.message).isEqualTo(
      """▼ Expect that "a string
        |              |with
        |              |line breaks":
        |  ✗ is equal to "a different string
        |                |with
        |                |line breaks"
        |          found "a string
        |                |with
        |                |line breaks"
      """.trimMargin()
    )
  }

  @Nested
  @DisplayName("subject description")
  inner class SubjectDescription {
    @Nested
    @DisplayName("when using get")
    inner class Get {
      @Test
      fun `is the property name when passing a lambda`() {
        val subject = Person("David", LocalDate.of(1947, 1, 8))

        val error = assertThrows<AssertionError> {
          expectThat(subject).get { birthDate }
            .get(LocalDate::getYear)
            .isEqualTo(1971)
        }

        expectThat(error.message).isEqualTo(
          """▼ Expect that Person(name=David, birthDate=1947-01-08):
          |  ▼ birthDate:
          |    ▼ return value of getYear:
          |      ✗ is equal to 1971
          |              found 1947""".trimMargin()
        )
      }

      @Test
      fun `is the property name when passing a property`() {
        val subject = Person("David", LocalDate.of(1947, 1, 8))

        val error = assertThrows<AssertionError> {
          expectThat(subject).get(Person::birthDate)
            .get(LocalDate::getYear)
            .isEqualTo(1971)
        }

        expectThat(error.message).isEqualTo(
          """▼ Expect that Person(name=David, birthDate=1947-01-08):
          |  ▼ value of property birthDate:
          |    ▼ return value of getYear:
          |      ✗ is equal to 1971
          |              found 1947""".trimMargin()
        )
      }
    }

    @Nested
    @DisplayName("when using with")
    inner class With {
      @Test
      fun `is the property name when passing a lambda`() {
        val subject = Person("David", LocalDate.of(1947, 1, 8))

        val error = assertThrows<AssertionError> {
          expectThat(subject) {
            with({ birthDate }) {
              get { year }.isEqualTo(1971)
            }
          }
        }

        expectThat(error.message).isEqualTo(
          """▼ Expect that Person(name=David, birthDate=1947-01-08):
          |  ▼ birthDate:
          |    ▼ year:
          |      ✗ is equal to 1971
          |              found 1947""".trimMargin()
        )
      }

      @Test
      fun `is property name when passing a property`() {
        val subject = Person("David", LocalDate.of(1947, 1, 8))

        val error = assertThrows<AssertionError> {
          expectThat(subject) {
            with(Person::birthDate) {
              get { year }.isEqualTo(1971)
            }
          }
        }

        expectThat(error.message).isEqualTo(
          """▼ Expect that Person(name=David, birthDate=1947-01-08):
          |  ▼ value of property birthDate:
          |    ▼ year:
          |      ✗ is equal to 1971
          |              found 1947""".trimMargin()
        )
      }
    }
  }
}
