package strikt

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.endsWith
import strikt.assertions.getValue
import strikt.assertions.hasLength
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isLessThan
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.single
import strikt.assertions.startsWith
import java.time.LocalDate

@DisplayName("assertions in blocks")
internal class Block {
  @Test
  fun `all assertions in a block are evaluated even if some fail`() {
    assertThrows<AssertionError> {
      val subject: Any? = "fnord"
      expectThat(subject) {
        isNull()
        isNotNull()
        isA<String>()
        isA<Number>()
      }
    }.let { error ->
      val expected = """
        |▼ Expect that "fnord":
        |  ✗ is null
        |  ✓ is not null
        |  ✓ is an instance of java.lang.String
        |  ✗ is an instance of java.lang.Number
        |                found java.lang.String"""
        .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  @Test
  fun `chains inside of blocks break on the first failure`() {
    assertThrows<AssertionError> {
      val subject: Any? = "fnord"
      expectThat(subject) {
        isNotNull()
        isA<Number>().isA<Long>()
        isEqualTo("fnord")
      }
    }.let { error ->
      val expected = """
        |▼ Expect that "fnord":
        |  ✓ is not null
        |  ✗ is an instance of java.lang.Number
        |                found java.lang.String
        |  ✓ is equal to "fnord""""
        .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  @Test
  fun `isNotNull inside a block breaks chain if it fails`() {
    assertThrows<AssertionError> {
      val subject: Any? = null
      expectThat(subject) {
        isNotNull().isEqualTo("fnord")
      }
    }.let { error ->
      val expected = """
        |▼ Expect that null:
        |  ✗ is not null"""
        .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  @Test
  fun `get chained after a failing assertion is not evaluated`() {
    assertThrows<AssertionError> {
      val subject: Any? = "fnord"
      expectThat(subject) {
        isA<Int>().get("multiplied by two") { this * 2 }.isGreaterThan(1)
      }
    }.let { error ->
      val expected = """
        |▼ Expect that "fnord":
        |  ✗ is an instance of java.lang.Integer
        |                found java.lang.String"""
        .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  @Test
  fun `assertions in a block can be negated`() {
    assertThrows<AssertionError> {
      val subject: Any? = "fnord"
      expectThat(subject) {
        not().isNull()
        not().isNotNull()
        not().isA<String>()
        not().isA<Number>()
      }
    }.let { error ->
      val expected = """
        |▼ Expect that "fnord":
        |  ✓ is not null
        |  ✗ is null
        |  ✗ is not an instance of java.lang.String
        |  ✓ is not an instance of java.lang.Number"""
        .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  @Test
  fun `assertions in a block can be negated in a not block`() {
    assertThrows<AssertionError> {
      val subject: Any? = "fnord"
      expectThat(subject).not {
        isNull()
        isNotNull()
        isA<String>()
        isA<Number>()
      }
    }.let { error ->
      val expected = """
        |▼ Expect that "fnord":
        |  ✓ is not null
        |  ✗ is null
        |  ✗ is not an instance of java.lang.String
        |  ✓ is not an instance of java.lang.Number"""
        .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  @Test
  fun `an and block can be negated`() {
    val subject: Any? = "fnord"
    expectThat(subject).not().and {
      isNull()
    }
  }

  @Test
  fun `contains can be negated`() {
    expectThat(listOf<String>()).not().contains("blah")
  }

  @Test
  fun `can evaluate a block of assertions on a derived subject`() {
    assertThrows<AssertionError> {
      val subject = Person("David", LocalDate.of(1947, 1, 8))
      expectThat(subject)
        .with("name", Person::name) {
          startsWith("Z")
          endsWith("y")
          hasLength(5)
        }
    }.let { error ->
      val expected = """
        |▼ Expect that Person(name=David, birthDate=1947-01-08):
        |  ▼ name:
        |    ✗ starts with "Z"
        |            found "D"
        |    ✗ ends with "y"
        |          found "d"
        |    ✓ has length 5"""
        .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  @Test
  fun `can evaluate a block of assertions with a contextual description on a derived subject`() {
    assertThrows<AssertionError> {
      val subject = Person("David", LocalDate.of(1947, 1, 8))
      expectThat(subject)
        .with(Person::name) {
          startsWith("Z")
          endsWith("y")
          hasLength(5)
        }
    }.let { error ->
      val expected = """
        |▼ Expect that Person(name=David, birthDate=1947-01-08):
        |  ▼ value of property name:
        |    ✗ starts with "Z"
        |            found "D"
        |    ✗ ends with "y"
        |          found "d"
        |    ✓ has length 5"""
        .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  @Test
  fun `can chain after a successful block of assertions on a derived subject`() {
    assertThrows<AssertionError> {
      val subject = Person("David", LocalDate.of(1947, 1, 8))
      expectThat(subject)
        .with(Person::birthDate) {
          isLessThan(LocalDate.of(2019, 11, 30))
        }
        .with(Person::name) {
          startsWith("Z")
          endsWith("y")
          hasLength(5)
        }
    }.let { error ->
      val expected = """
        |▼ Expect that Person(name=David, birthDate=1947-01-08):
        |  ▼ value of property birthDate:
        |    ✓ is less than 2019-11-30
        |  ▼ value of property name:
        |    ✗ starts with "Z"
        |            found "D"
        |    ✗ ends with "y"
        |          found "d"
        |    ✓ has length 5"""
        .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  @Test
  fun `chain after a failing block of assertions on a derived subject is not evaluated`() {
    assertThrows<AssertionError> {
      val subject = Person("David", LocalDate.of(1947, 1, 8))
      expectThat(subject)
        .with(Person::name) {
          startsWith("Z")
          endsWith("y")
          hasLength(5)
        }
        .with(Person::birthDate) {
          isLessThan(LocalDate.of(2019, 11, 30))
        }
    }.let { error ->
      val expected = """
        |▼ Expect that Person(name=David, birthDate=1947-01-08):
        |  ▼ value of property name:
        |    ✗ starts with "Z"
        |            found "D"
        |    ✗ ends with "y"
        |          found "d"
        |    ✓ has length 5"""
        .trimMargin()
      expectThat(error.message).isEqualTo(expected)
    }
  }

  /**
   * @see https://github.com/robfletcher/strikt/issues/203
   */
  @Test
  fun `failing mappings inside an expectThat block throw correct exception type`() {
    assertThrows<AssertionError> {
      class Person(val firstName: String, val hobbies: List<String>)

      // imagine that "p" is coming from some API call and needs to be nullable
      val p = Person("John", listOf("a", "b"))

      expectThat(p) {
        get { firstName }.isEqualTo("John")
        get { hobbies }.single().isA<String>().isEqualTo("a")
      }
    }
  }

  /**
   * @see https://github.com/robfletcher/strikt/issues/203
   */
  @Test
  fun `failing mappings inside an and block throw correct exception type`() {
    assertThrows<AssertionError> {
      class Person(val firstName: String, val hobbies: List<String>)

      // imagine that "p" is coming from some API call and needs to be nullable
      val p: Person? = Person("John", listOf("a", "b"))

      expectThat(p).isNotNull().and {
        get { firstName }.isEqualTo("John")
        get { hobbies }.single().isA<String>().isEqualTo("a")
      }
    }
  }

  fun `failing nested isNotNull does not evaluate following assertion`() {
    assertThrows<AssertionError> {
      val subject = mapOf(
        "word1" to "catflap",
        "word2" to "rubberplant",
        "word3" to null
      )
      expectThat(subject) {
        getValue("word1").isNotNull().isEqualTo("catflap")
        getValue("word2").isNotNull().isEqualTo("rubberplant")
        getValue("word3").isNotNull().isEqualTo("marzipan")
      }
    }
  }

  /**
   * @see https://github.com/robfletcher/strikt/issues/204
   */
  @Test
  fun `nested and is not evaluated if preceding assertion failed`() {
    assertThrows<AssertionError> {
      class Person(val name: String, val friend: Person?)
      // imagine we get this from an API which is nullable
      val p: Person? = Person("John", null)

      expectThat(p).isNotNull().and {
        get { name }.isEqualTo("John")
        get { friend }.isNotNull().and {
          get { name }.isEqualTo("Jack")
        }
      }
    }
  }
}
