package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import strikt.api.Assertion.Builder
import strikt.api.expect
import strikt.fails

@DisplayName("assertions on Any")
internal class AnyAssertions {
  @Nested
  @DisplayName("isNull assertion")
  inner class IsNull {
    @Test
    fun `passes if the subject is null`() {
      val subject: Any? = null
      expect(subject).isNull()
    }

    @Test
    fun `fails if the subject is not null`() {
      fails {
        val subject: Any? = "fnord"
        expect(subject).isNull()
      }
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `down-casts the result`() {
      val subject: Any? = null
      expect(subject)
        .also { assert(it is Builder<Any?>) }
        .isNull()
        .also { assert(it is Builder<Nothing>) }
    }
  }

  @Nested
  @DisplayName("isNotNull assertion")
  inner class IsNotNull {
    @Test
    fun `fails if the subject is null`() {
      fails {
        val subject: Any? = null
        expect(subject).isNotNull()
      }
    }

    @Test
    fun `passes if the subject is not null`() {
      val subject: Any? = "fnord"
      expect(subject).isNotNull()
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `down-casts the result`() {
      val subject: Any? = "fnord"
      expect(subject)
        .also { assert(it is Builder<Any?>) }
        .isNotNull()
        .also { assert(it is Builder<Any>) }
    }
  }

  @Nested
  @DisplayName("isA assertion")
  inner class IsA {
    @Test
    fun `fails if the subject is null`() {
      fails {
        val subject: Any? = null
        expect(subject).isA<String>()
      }
    }

    @Test
    fun `fails if the subject is a different type`() {
      fails {
        val subject = 1L
        expect(subject).isA<String>()
      }
    }

    @Test
    fun `passes if the subject is the same exact type`() {
      val subject = "fnord"
      expect(subject).isA<String>()
    }

    @Test
    fun `passes if the subject is a sub-type`() {
      val subject: Any = 1L
      expect(subject).isA<Number>()
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `down-casts the result`() {
      val subject: Any = 1L
      expect(subject)
        .also { assert(it is Builder<Any>) }
        .isA<Number>()
        .also { assert(it is Builder<Number>) }
        .isA<Long>()
        .also { assert(it is Builder<Long>) }
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `allows specialized assertions after establishing type`() {
      val subject: Any = "fnord"
      expect(subject)
        .also { assert(it is Builder<Any>) }
        .isA<String>()
        .also { assert(it is Builder<String>) }
        .hasLength(5) // only available on Assertion<CharSequence>
    }
  }

  @Nested
  @DisplayName("isEqualTo assertion")
  inner class IsEqualTo {
    @Test
    fun `passes if the subject matches the expectation`() {
      expect("fnord").isEqualTo("fnord")
    }

    @TestFactory
    fun `fails subject is equal to expected`() =
      listOf(
        Pair("fnord", "FNORD"),
        Pair(1, 1L),
        Pair(null, "fnord"),
        Pair("fnord", null)
      ).map { (subject, expected) ->
        dynamicTest("fails $subject is equal to $expected") {
          fails {
            expect(subject).isEqualTo(expected)
          }
        }
      }

    @Test
    fun `specifies type information if the values look the same`() {
      fails {
        expect<Number>(5L).isEqualTo(5)
      }.let { e ->
        assertEquals(
          """▼ Expect that 5:
  ✗ is equal to 5 (Int) : found 5 (Long)""", e.message
        )
      }
    }
  }

  @Nested
  @DisplayName("isNotEqualTo assertion")
  inner class IsNotEqualTo {
    @Test
    fun `fails if the subject matches the expectation`() {
      fails {
        expect("fnord").isNotEqualTo("fnord")
      }
    }

    @TestFactory
    fun `passes subject is not equal to expected`() =
      listOf(
        Pair("fnord", "FNORD"),
        Pair(1, 1L),
        Pair(null, "fnord"),
        Pair("fnord", null)
      ).map { (subject, expected) ->
        dynamicTest("passes $subject is not equal to $expected") {
          expect(subject).isNotEqualTo(expected)
        }
      }

    @Nested
    @DisplayName("isSameInstanceAs assertion")
    inner class IsSameInstanceAs {

      @TestFactory
      fun `fails subject is not the same instance as expected`() =
        listOf(
          Pair(listOf("fnord"), listOf("fnord")),
          Pair(null, listOf("fnord")),
          Pair(listOf("fnord"), null),
          Pair(1, 1L)
        ).map { (subject, expected) ->
          dynamicTest("fails {0} is not the same instance as {1}") {
            fails {
              expect(subject).isSameInstanceAs(expected)
            }
          }
        }

      @TestFactory
      fun `succeeds subject is the same instance as expected`() =
        listOf(
          Pair("fnord", "fnord"),
          Pair(1L, 1L),
          Pair(null, null),
          listOf("fnord").let
          { Pair(it, it) }
        ).map { (subject, expected) ->
          dynamicTest("passes {0} is same instance as {1}") {
            expect(subject).isSameInstanceAs(expected)
          }
        }

      @Nested
      @DisplayName("isNotSameInstanceAs assertion")
      inner class IsNotSameInstanceAs {

        @TestFactory
        fun `succeeds subject is not the same instance as expected`() =
          listOf(
            Pair(listOf("fnord"), listOf("fnord")),
            Pair(null, listOf("fnord")),
            Pair(listOf("fnord"), null),
            Pair(1, 1L)
          ).map { (subject, expected) ->
            dynamicTest("passes {0} is not same instance as to {1}") {
              expect(subject).isNotSameInstanceAs(expected)
            }
          }

        @TestFactory
        fun `fails subject is not the same instance as expected`() =
          listOf(
            Pair("fnord", "fnord"),
            Pair(1L, 1L),
            Pair(null, null),
            listOf("fnord").let
            { Pair(it, it) }
          ).map { (subject, expected) ->
            dynamicTest("fails {0} is not same instance as {1}") {
              fails {
                expect(subject).isNotSameInstanceAs(expected)
              }
            }
          }
      }
    }
  }
}
