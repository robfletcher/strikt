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
  fun isNull() = junitTests<Any?> {
    context("a null subject") {
      fixture { null }

      test("the assertion passes") {
        expectThat(this).isNull()
      }

      test("the assertion down-casts the subject") {
        @Suppress("USELESS_IS_CHECK")
        expectThat(this)
          .also { assert(it is Builder<Any?>) }
          .isNull()
          .also { assert(it is Builder<Nothing>) }
      }
    }

    context("a non-null subject") {
      fixture { "fnord" }

      test("the assertion fails") {
        fails {
          expectThat(this).isNull()
        }
      }
    }
  }

  @TestFactory
  fun isNotNull() = junitTests<Any?> {
    context("a null subject") {
      fixture { null }

      test("the assertion fails") {
        fails {
          expectThat(this).isNotNull()
        }
      }
    }

    context("a non-null subject") {
      fixture { "fnord" }

      test("the assertion passes") {
        expectThat(this).isNotNull()
      }

      test("down-casts the result") {
        @Suppress("USELESS_IS_CHECK")
        expectThat(this)
          .also { assert(it is Builder<Any?>) }
          .isNotNull()
          .also { assert(it is Builder<Any>) }
      }
    }
  }

  @TestFactory
  fun isA() = junitTests<Any?> {
    context("a null subject") {
      fixture { null }

      test("the assertion fails") {
        fails {
          expectThat(this).isA<String>()
        }
      }
    }

    context("a subject of the wrong type") {
      fixture { 1L }

      test("the assertion fails") {
        fails {
          expectThat(this).isA<String>()
        }
      }
    }

    context("a subject of the same type as the expected type") {
      fixture { "fnord" }

      test("the assertion passes") {
        expectThat(this).isA<String>()
      }

      test("the assertion narrows the subject type") {
        @Suppress("USELESS_IS_CHECK")
        expectThat(this)
          .also { assert(it is Builder<Any?>) }
          .isA<String>()
          .also { assert(it is Builder<String>) }
      }

      test("the narrowed type can use specialized assertions") {
        @Suppress("USELESS_IS_CHECK")
        expectThat(this)
          .also { assert(it is Builder<Any?>) }
          .isA<String>()
          .also { assert(it is Builder<String>) }
          .hasLength(5) // only available on Assertion<CharSequence>
      }
    }

    context("a subject that is a sub-type of the expected type") {
      fixture { 1L }

      test("the assertion passes") {
        expectThat(this).isA<Number>()
      }

      test("the assertion narrows the subject type") {
        @Suppress("USELESS_IS_CHECK")
        expectThat(this)
          .also { assert(it is Builder<Any?>) }
          .isA<Number>()
          .also { assert(it is Builder<Number>) }
          .isA<Long>()
          .also { assert(it is Builder<Long>) }
      }
    }
  }

  @TestFactory
  fun isEqualTo() = junitTests<Pair<Any?, Any?>> {
    context("the subject matches the expectation") {
      fixture { "fnord" to "fnord" }

      test("the assertion passes") {
        expectThat(first).isEqualTo(second)
      }
    }

    listOf(
      "fnord" to "FNORD",
      1 to 1L,
      null to "fnord",
      "fnord" to null
    ).map {
      context("subject is ${it.first.quoted()} and expected value is ${it.second.quoted()}") {
        fixture { it }

        test("the assertion fails") {
          fails {
            expectThat(first).isEqualTo(second)
          }
        }
      }
    }

    context("subject is a different type but looks the same") {
      fixture { 5L to 5 }

      test("the failure message specifies the types involved") {
        val e = fails {
          expectThat(first).isEqualTo(second)
        }
        assertEquals(
          """▼ Expect that 5:
  ✗ is equal to 5 (Int) : found 5 (Long)""", e.message
        )
      }
    }
  }

  @TestFactory
  fun isNotEqualTo() = junitTests<Pair<Any?, Any?>> {
    context("the subject matches the expectation") {
      fixture { "fnord" to "fnord" }

      test("the assertion fails") {
        fails {
          expectThat(first).isNotEqualTo(second)
        }
      }
    }

    listOf(
      "fnord" to "FNORD",
      1 to 1L,
      null to "fnord",
      "fnord" to null
    ).map {
      context("subject is ${it.first.quoted()} and expected value is ${it.second.quoted()}") {
        fixture { it }

        test("the assertion passes") {
          expectThat(first).isNotEqualTo(second)
        }
      }
    }
  }

  @TestFactory
  fun isSameInstanceAs() = junitTests<Pair<Any?, Any?>> {
    listOf(
      listOf("fnord") to listOf("fnord"),
      null to listOf("fnord"),
      listOf("fnord") to null,
      1 to 1L
    ).map {
      context("${it.first.quoted()} is not the same instance as ${it.second.quoted()}") {
        fixture { it }

        test("the assertion fails") {
          fails {
            expectThat(first).isSameInstanceAs(second)
          }
        }
      }
    }

    listOf("fnord", 1L, null, listOf("fnord"))
      .map {
        context("${it.quoted()} is the same instance as itself") {
          fixture { it to it }

          test("the assertion passes") {
            expectThat(first).isSameInstanceAs(second)
          }
        }
      }
  }

  @TestFactory
  fun isNotSameInstanceAs() = junitTests<Pair<Any?, Any?>> {
    listOf(
      listOf("fnord") to listOf("fnord"),
      null to listOf("fnord"),
      listOf("fnord") to null,
      1 to 1L
    ).map {
      context("${it.first.quoted()} is not the same instance as ${it.second.quoted()}") {
        fixture { it }

        test("the assertion passes") {
          expectThat(first).isNotSameInstanceAs(second)
        }
      }
    }

    listOf("fnord", 1L, null, listOf("fnord"))
      .map {
        context("${it.quoted()} is not the same instance as itself") {
          fixture { it to it }

          test("the assertion fails") {
            fails {
              expectThat(first).isNotSameInstanceAs(second)
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
