package strikt.assertions

import com.oneeyedmen.minutest.junit.junitTests
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import strikt.api.Assertion.Builder
import strikt.api.expectThat
import strikt.fails

@DisplayName("assertions on Any")
internal object AnyAssertions {

  @TestFactory
  fun isNull() = junitTests<Unit> {
    context("a null subject") {
      val subject: Any? = null

      test("the assertion passes") {
        expectThat(subject).isNull()
      }

      test("the assertion down-casts the subject") {
        @Suppress("USELESS_IS_CHECK")
        expectThat(subject)
          .also { assert(it is Builder<Any?>) }
          .isNull()
          .also { assert(it is Builder<Nothing>) }
      }
    }

    context("a non-null subject") {
      val subject: Any? = "fnord"

      test("the assertion fails") {
        fails {
          expectThat(subject).isNull()
        }
      }
    }
  }

  @TestFactory
  fun isNotNull() = junitTests<Unit> {
    context("a null subject") {
      val subject: Any? = null

      test("the assertion fails") {
        fails {
          expectThat(subject).isNotNull()
        }
      }
    }

    context("a non-null subject") {
      val subject: Any? = "fnord"

      test("the assertion passes") {
        expectThat(subject).isNotNull()
      }

      test("down-casts the result") {
        @Suppress("USELESS_IS_CHECK")
        expectThat(subject)
          .also { assert(it is Builder<Any?>) }
          .isNotNull()
          .also { assert(it is Builder<Any>) }
      }
    }
  }

  @TestFactory
  fun isA() = junitTests<Unit> {
    context("a null subject") {
      val subject: Any? = null

      test("the assertion fails") {
        fails {
          expectThat(subject).isA<String>()
        }
      }
    }

    context("a subject of the wrong type") {
      val subject: Any? = 1L

      test("the assertion fails") {
        fails {
          expectThat(subject).isA<String>()
        }
      }
    }

    context("a subject of the same type as the expected type") {
      val subject: Any? = "fnord"

      test("the assertion passes") {
        expectThat(subject).isA<String>()
      }

      test("the assertion narrows the subject type") {
        @Suppress("USELESS_IS_CHECK")
        expectThat(subject)
          .also { assert(it is Builder<Any?>) }
          .isA<String>()
          .also { assert(it is Builder<String>) }
      }

      test("the narrowed type can use specialized assertions") {
        @Suppress("USELESS_IS_CHECK")
        expectThat(subject)
          .also { assert(it is Builder<Any?>) }
          .isA<String>()
          .also { assert(it is Builder<String>) }
          .hasLength(5) // only available on Assertion<CharSequence>
      }
    }

    context("a subject that is a sub-type of the expected type") {
      val subject: Any? = 1L

      test("the assertion passes") {
        expectThat(subject).isA<Number>()
      }

      test("the assertion narrows the subject type") {
        @Suppress("USELESS_IS_CHECK")
        expectThat(subject)
          .also { assert(it is Builder<Any?>) }
          .isA<Number>()
          .also { assert(it is Builder<Number>) }
          .isA<Long>()
          .also { assert(it is Builder<Long>) }
      }
    }
  }

  @TestFactory
  fun isEqualTo() = junitTests<Unit> {
    context("the subject matches the expectation") {
      val subject: Any? = "fnord"

      test("the assertion passes") {
        expectThat(subject).isEqualTo("fnord")
      }
    }

    listOf(
      Pair("fnord", "FNORD"),
      Pair(1, 1L),
      Pair(null, "fnord"),
      Pair("fnord", null)
    ).map { (subject, expected) ->
      context("subject is ${subject.quoted()} and expected value is ${expected.quoted()}") {
        test("the assertion fails") {
          fails {
            expectThat(subject).isEqualTo(expected)
          }
        }
      }
    }

    context("subject is a different type but looks the same") {
      val subject = 5L
      val expected = 5

      test("the failure message specifies the types involved") {
        val e = fails {
          expectThat<Number>(subject).isEqualTo(expected)
        }
        assertEquals(
          """▼ Expect that 5:
  ✗ is equal to 5 (Int) : found 5 (Long)""", e.message
        )
      }
    }
  }

  @TestFactory
  fun isNotEqualTo() = junitTests<Unit> {
    context("the subject matches the expectation") {
      val subject: Any? = "fnord"

      test("the assertion fails") {
        fails {
          expectThat(subject).isNotEqualTo("fnord")
        }
      }
    }

    listOf(
      Pair("fnord", "FNORD"),
      Pair(1, 1L),
      Pair(null, "fnord"),
      Pair("fnord", null)
    ).map { (subject, expected) ->
      context("subject is ${subject.quoted()} and expected value is ${expected.quoted()}") {
        test("the assertion passes") {
          expectThat(subject).isNotEqualTo(expected)
        }
      }
    }
  }

  @TestFactory
  fun isSameInstanceAs() = junitTests<Unit> {
    listOf(
      Pair(listOf("fnord"), listOf("fnord")),
      Pair(null, listOf("fnord")),
      Pair(listOf("fnord"), null),
      Pair(1, 1L)
    ).map { (subject, expected) ->
      context("${subject.quoted()} is not the same instance as ${expected.quoted()}") {
        test("the assertion fails") {
          fails {
            expectThat(subject).isSameInstanceAs(expected)
          }
        }
      }
    }

    listOf(
      Pair("fnord", "fnord"),
      Pair(1L, 1L),
      Pair(null, null),
      listOf("fnord").let
      { Pair(it, it) }
    ).map { (subject, expected) ->
      context("${subject.quoted()} is the same instance as ${expected.quoted()}") {
        test("the assertion passes") {
          expectThat(subject).isSameInstanceAs(expected)
        }
      }
    }
  }

  @TestFactory
  fun isNotSameInstanceAs() = junitTests<Unit> {
    listOf(
      Pair(listOf("fnord"), listOf("fnord")),
      Pair(null, listOf("fnord")),
      Pair(listOf("fnord"), null),
      Pair(1, 1L)
    ).map { (subject, expected) ->
      context("${subject.quoted()} is not the same instance as ${expected.quoted()}") {
        test("the assertion passes") {
          expectThat(subject).isNotSameInstanceAs(expected)
        }
      }
    }

    listOf(
      Pair("fnord", "fnord"),
      Pair(1L, 1L),
      Pair(null, null),
      listOf("fnord").let
      { Pair(it, it) }
    ).map { (subject, expected) ->
      context("${subject.quoted()} is not the same instance as ${expected.quoted()}") {
        test("the assertion fails") {
          fails {
            expectThat(subject).isNotSameInstanceAs(expected)
          }
        }
      }
    }
  }

  private fun Any?.quoted(): String = when (this) {
    null -> "null"
    else -> "\"$this\""
  }
}
