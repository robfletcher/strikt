package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

@DisplayName("assertions on Iterable")
internal class IterableAssertions {

  /**
   * Turns a list subject into various iterable types with the same content.
   */
  fun <E : Comparable<E>> List<E>.permute(): List<Iterable<E>> =
    listOf(
      this,
      toSet(),
      toSortedSet()
    )

  /**
   * Turns a list subject with expected values into various iterable types with
   * the same content and the same expected value.
   */
  fun <E : Comparable<E>, EX> List<Pair<List<E>, EX>>.permuteExpected(): List<Pair<Iterable<E>, EX>> =
    flatMap {
      listOf(
        it.first to it.second,
        it.first.toSet() to it.second,
        it.first.toSortedSet() to it.second
      )
    }

  @Nested
  @DisplayName("all assertion")
  inner class All {
    @TestFactory
    fun `passes if all elements conform`() =
      listOf("catflap", "rubberplant", "marzipan").permute()
        .map { subject ->
          dynamicTest("passes if all elements of a ${subject.javaClass.simpleName} conform") {
            expectThat(subject).all {
              isLowerCase()
            }
          }
        }

    @TestFactory
    fun `fails if any element does not conform`() =
      listOf("catflap", "rubberplant", "marzipan").permute()
        .map { subject ->
          dynamicTest("fails if any element of a ${subject.javaClass.simpleName} conform") {
            assertThrows<AssertionError> {
              expectThat(subject).all {
                startsWith('c')
              }
            }
          }
        }
  }

  @Nested
  @DisplayName("any assertion")
  inner class Any {
    @TestFactory
    fun `passes if all elements conform`() =
      listOf("catflap", "rubberplant", "marzipan").permute()
        .map { subject ->
          dynamicTest("passes if all elements of a ${subject.javaClass.simpleName} conform") {
            expectThat(subject).any {
              isLowerCase()
            }
          }
        }

    @TestFactory
    fun `passes if any one element conforms`() =
      listOf("catflap", "RUBBERPLANT", "MARZIPAN").permute()
        .map { subject ->
          dynamicTest("passes if any one element of a ${subject.javaClass.simpleName} conforms") {
            expectThat(subject).any {
              isLowerCase()
            }
          }
        }

    @TestFactory
    fun `fails if no elements conform`() =
      listOf("CATFLAP", "RUBBERPLANT", "MARZIPAN").permute()
        .map { subject ->
          dynamicTest("fails if no elements of a ${subject.javaClass.simpleName} conform") {
            assertThrows<AssertionError> {
              expectThat(subject).any {
                isLowerCase()
              }
            }
          }
        }

    @Test
    fun `works with not`() {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expectThat(subject).not().any {
        isUpperCase()
      }
    }
  }

  @Nested
  @DisplayName("none assertion")
  inner class None {
    @TestFactory
    fun `passes if no elements conform`() =
      listOf("catflap", "rubberplant", "marzipan").permute()
        .map { subject ->
          dynamicTest("passes if no elements of a ${subject.javaClass.simpleName} conform") {
            expectThat(subject).none {
              isUpperCase()
            }
          }
        }

    @TestFactory
    fun `fails if some elements conforms`() =
      listOf("catflap", "RUBBERPLANT", "MARZIPAN").permute()
        .map { subject ->
          dynamicTest("fails if some elements of a ${subject.javaClass.simpleName} conforms") {
            assertThrows<AssertionError> {
              expectThat(subject).none {
                isUpperCase()
              }
            }
          }
        }

    @TestFactory
    fun `fails if all elements conform`() =
      listOf("CATFLAP", "RUBBERPLANT", "MARZIPAN").permute()
        .map { subject ->
          dynamicTest("fails if all elements of a ${subject.javaClass.simpleName} conform") {
            assertThrows<AssertionError> {
              expectThat(subject).none {
                isUpperCase()
              }
            }
          }
        }

    @Test
    fun `works with not`() {
      val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
      expectThat(subject).not().none {
        isUpperCase()
      }
    }
  }

  @Nested
  @DisplayName("at least assertion")
  inner class AtLeast {
    @TestFactory
    fun `fails if not enough elements conform`() =
      listOf("catflap", "rubberplant", "MARZIPAN").permute()
        .map { subject ->
          dynamicTest("fails if not enough elements of a ${subject.javaClass.simpleName} conform") {
            assertThrows<AssertionError> {
              expectThat(subject).atLeast(2) {
                isUpperCase()
              }
            }
          }
        }

    @TestFactory
    fun `passes if exactly enough elements conform`() =
      listOf("catflap", "RUBBERPLANT", "MARZIPAN").permute()
        .map { subject ->
          dynamicTest("fails if not enough elements of a ${subject.javaClass.simpleName} conforms") {
            expectThat(subject).atLeast(2) {
              isUpperCase()
            }
          }
        }

    @TestFactory
    fun `passes if all elements conform`() =
      listOf("CATFLAP", "RUBBERPLANT", "MARZIPAN").permute()
        .map { subject ->
          dynamicTest("passes if all elements of a ${subject.javaClass.simpleName} conform") {
            expectThat(subject).atLeast(2) {
              isUpperCase()
            }
          }
        }

    @Test
    fun `works with not`() {
      val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
      assertThrows<AssertionError> {
        expectThat(subject).not().atLeast(2) {
          isUpperCase()
        }
      }
    }
  }

  @Nested
  @DisplayName("contains assertion")
  inner class Contains {
    @TestFactory
    fun `passes subject contains expected`() =
      listOf(
        listOf("catflap") to arrayOf("catflap"),
        listOf("catflap", "rubberplant", "marzipan") to arrayOf("catflap"),
        listOf("catflap", "rubberplant", "marzipan") to arrayOf(
          "catflap",
          "marzipan"
        )
      )
        .permuteExpected()
        .map { (subject, expected) ->
          dynamicTest("passes if $subject (${subject.javaClass.simpleName}) contains ${expected.toList()}") {
            expectThat(subject).contains(*expected)
          }
        }

    @TestFactory
    fun `fails subject contains expected`() =
      listOf(
        listOf("catflap", "rubberplant", "marzipan") to arrayOf("fnord"),
        listOf("catflap", "rubberplant", "marzipan") to arrayOf(
          "catflap",
          "fnord"
        ),
        emptyList<String>() to arrayOf("catflap")
      )
        .permuteExpected()
        .map { (subject, expected) ->
          dynamicTest("fails $subject (${subject.javaClass.simpleName}) contains ${expected.toList()}") {
            assertThrows<AssertionError> {
              expectThat(subject).contains(*expected)
            }
          }
        }

    @Test
    fun `any collection contains an empty list`() {
      expectThat(listOf("catflap", "rubberplant", "marzipan")).contains()
    }

    @Test
    fun `an empty collection contains an empty list`() {
      expectThat(emptyList<Any>()).contains()
    }

    @Test
    fun `has a nested failure for each missing element when there are multiple`() {
      val error = assertThrows<AssertionError> {
        expectThat(listOf("catflap", "rubberplant", "marzipan"))
          .contains("fnord", "marzipan", "bojack")
      }
      assertEquals(
        """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
          |  ✗ contains the elements ["fnord", "marzipan", "bojack"]
          |    ✗ contains "fnord"
          |    ✓ contains "marzipan"
          |    ✗ contains "bojack"""".trimMargin(),
        error.message
      )
    }

    @Test
    fun `does not nest failures when there is only one element`() {
      val error = assertThrows<AssertionError> {
        expectThat(listOf("catflap", "rubberplant", "marzipan"))
          .contains("fnord")
      }
      assertEquals(
        "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
          "  ✗ contains \"fnord\"",
        error.message
      )
    }
  }

  @Nested
  @DisplayName("doesNotContain assertion")
  inner class DoesNotContain {
    @TestFactory
    fun `always passes for an empty subject`() =
      emptyList<String>()
        .permute()
        .map { subject ->
          dynamicTest("always passes for an empty ${subject.javaClass.simpleName}") {
            expectThat(subject)
              .doesNotContain("catflap", "rubberplant", "marzipan")
          }
        }

    @TestFactory
    fun `fails if no elements are specified`() =
      listOf(
        emptyList(),
        listOf("catflap", "rubberplant", "marzipan")
      )
        .flatMap { it.permute() }
        .map { subject ->
          dynamicTest("fails for $subject is if no elements are specified") {
            assertThrows<IllegalArgumentException> {
              expectThat(subject).doesNotContain()
            }
          }
        }

    @TestFactory
    fun `passes if the subject contains none of the elements`() =
      listOf(
        listOf("catflap", "rubberplant", "marzipan") to arrayOf("fnord"),
        listOf("catflap", "rubberplant", "marzipan") to arrayOf(
          "xenocracy",
          "wye",
          "exercitation"
        )
      )
        .permuteExpected()
        .map { (subject, elements) ->
          dynamicTest("passes if a ${subject.javaClass.simpleName} contains none of the elements ${elements.toList()}") {
            expectThat(subject)
              .doesNotContain(*elements)
          }
        }

    @TestFactory
    fun `passes if the subject contains any of the elements`() =
      listOf(
        listOf("catflap", "rubberplant", "marzipan") to arrayOf("catflap"),
        listOf("catflap", "rubberplant", "marzipan") to arrayOf(
          "catflap",
          "kakistocracy",
          "impeach"
        ),
        listOf("catflap", "rubberplant", "marzipan") to arrayOf(
          "owlbear",
          "marzipan",
          "illithid"
        )
      )
        .permuteExpected()
        .map { (subject, elements) ->
          dynamicTest("passes if a ${subject.javaClass.simpleName} contains any of the elements ${elements.toList()}") {
            assertThrows<AssertionError> {
              expectThat(subject)
                .doesNotContain(*elements)
            }
          }
        }

    @Test
    fun `formats its failure message correctly when there are multiple elements`() {
      val error = assertThrows<AssertionError> {
        expectThat(listOf("catflap", "rubberplant", "marzipan"))
          .doesNotContain("catflap", "wye", "marzipan")
      }
      assertEquals(
        """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
          |  ✗ does not contain any of the elements ["catflap", "wye", "marzipan"]
          |    ✗ does not contain "catflap"
          |    ✓ does not contain "wye"
          |    ✗ does not contain "marzipan"""".trimMargin(),
        error.message
      )
    }

    @Test
    fun `formats its failure message correctly when there is a single element`() {
      val error = assertThrows<AssertionError> {
        expectThat(listOf("catflap", "rubberplant", "marzipan"))
          .doesNotContain("catflap")
      }
      assertEquals(
        "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
          "  ✗ does not contain \"catflap\"",
        error.message
      )
    }
  }

  @Nested
  @DisplayName("containsExactly assertion")
  inner class ContainsExactly {
    @Nested
    @DisplayName("a Set subject")
    inner class Set {
      val subject = setOf("catflap", "rubberplant", "marzipan")

      @Test
      fun `passes if the elements are identical`() {
        expectThat(subject)
          .containsExactly("catflap", "rubberplant", "marzipan")
      }

      @Test
      fun `fails if there are more elements than expected`() {
        assertThrows<AssertionError> {
          expectThat(subject).containsExactly("rubberplant", "catflap")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
        }
      }

      @Test
      fun `fails if the order is different (even though this is a Set)`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactly("rubberplant", "catflap", "marzipan")
        }
      }
    }

    @Nested
    @DisplayName("a List subject")
    inner class List {
      val subject = listOf("catflap", "rubberplant", "marzipan")

      @Test
      fun `passes if all the elements exist in the same order`() {
        expectThat(subject)
          .containsExactly("catflap", "rubberplant", "marzipan")
      }

      @Test
      fun `fails if there are more elements than expected`() {
        val error = assertThrows<AssertionError> {
          expectThat(subject).containsExactly("catflap", "rubberplant")
        }
        assertEquals(
          """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant"]
            |    ✓ contains "catflap"
            |    ✓ …at index 0
            |    ✓ contains "rubberplant"
            |    ✓ …at index 1
            |    ✗ contains no further elements : found ["marzipan"]""".trimMargin(),
          error.message
        )
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        val error = assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
        }
        assertEquals(
          """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant", "marzipan", "fnord"]
            |    ✓ contains "catflap"
            |    ✓ …at index 0
            |    ✓ contains "rubberplant"
            |    ✓ …at index 1
            |    ✓ contains "marzipan"
            |    ✓ …at index 2
            |    ✗ contains "fnord"
            |    ✓ contains no further elements""".trimMargin(),
          error.message
        )
      }

      /**
       * @see https://github.com/robfletcher/strikt/issues/159
       */
      @Test
      fun `fails if there are fewer elements than expected but the outlier is in the actual list`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactly("fnord", "catflap", "rubberplant", "marzipan")
        }
      }

      @Test
      fun `fails if the order is different`() {
        val error = assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactly("rubberplant", "catflap", "marzipan")
        }
        assertEquals(
          """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["rubberplant", "catflap", "marzipan"]
            |    ✓ contains "rubberplant"
            |    ✗ …at index 0 : found "catflap"
            |    ✓ contains "catflap"
            |    ✗ …at index 1 : found "rubberplant"
            |    ✓ contains "marzipan"
            |    ✓ …at index 2
            |    ✓ contains no further elements""".trimMargin(),
          error.message
        )
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        val error = assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactly("catflap", "rubberplant", "marzipan", "marzipan")
        }
        assertEquals(
          """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant", "marzipan", "marzipan"]
            |    ✓ contains "catflap"
            |    ✓ …at index 0
            |    ✓ contains "rubberplant"
            |    ✓ …at index 1
            |    ✓ contains "marzipan"
            |    ✓ …at index 2
            |    ✗ contains "marzipan"
            |    ✓ contains no further elements""".trimMargin(),
          error.message
        )
      }
    }

    @Nested
    @DisplayName("a non-Collection Iterable subject")
    inner class NonCollection {
      val subject = object : Iterable<String> {
        override fun iterator() =
          arrayOf("catflap", "rubberplant", "marzipan").iterator()
      }

      @Test
      fun `passes if the elements are indentical`() {
        expectThat(subject)
          .describedAs("a non-Collection iterable %s")
          .containsExactly("catflap", "rubberplant", "marzipan")
      }

      @Test
      fun `fails if the elements are ordered differently`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("marzipan", "rubberplant", "catflap")
        }
      }

      @Test
      fun `fails if there are more elements than expected`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("catflap", "rubberplant")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
        }
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("catflap", "rubberplant", "marzipan", "marzipan")
        }
      }

      @Test
      fun `fails if it's supposed to be empty and isn't`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly()
        }
      }

      @Test
      fun `passes if it's supposed to be empty and is`() {
        val emptySubject = object : Iterable<String> {
          override fun iterator() = emptySequence<String>().iterator()
        }
        expectThat(emptySubject).containsExactly()
      }
    }
  }

  @Nested
  @DisplayName("containsExactlyInAnyOrder assertion")
  inner class ContainsExactlyInAnyOrder {
    @Nested
    @DisplayName("a Set subject")
    inner class Set {
      val subject = setOf("catflap", "rubberplant", "marzipan")

      @Test
      fun `passes if the elements are identical`() {
        expectThat(subject)
          .containsExactlyInAnyOrder("rubberplant", "catflap", "marzipan")
      }

      @Test
      fun `fails if there are more elements than expected`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactlyInAnyOrder("rubberplant", "catflap")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "fnord"
            )
        }
      }
    }

    @Nested
    @DisplayName("a List subject")
    inner class List {
      val subject = listOf("catflap", "rubberplant", "marzipan")

      @Test
      fun `passes if all the elements exist in the same order`() {
        expectThat(subject)
          .containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan")
      }

      @Test
      fun `fails if there are more elements than expected`() {
        val error = assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactlyInAnyOrder("catflap", "rubberplant")
        }
        assertEquals(
          """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant"] in any order
            |    ✓ contains "catflap"
            |    ✓ contains "rubberplant"
            |    ✗ contains no further elements : found ["marzipan"]""".trimMargin(),
          error.message
        )
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        val error = assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "marzipan"
            )
        }
        assertEquals(
          """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant", "marzipan", "marzipan"] in any order
            |    ✓ contains "catflap"
            |    ✓ contains "rubberplant"
            |    ✓ contains "marzipan"
            |    ✗ contains "marzipan"
            |    ✓ contains no further elements""".trimMargin(),
          error.message
        )
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        val error = assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "fnord"
            )
        }
        assertEquals(
          """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant", "marzipan", "fnord"] in any order
            |    ✓ contains "catflap"
            |    ✓ contains "rubberplant"
            |    ✓ contains "marzipan"
            |    ✗ contains "fnord"
            |    ✓ contains no further elements""".trimMargin(),
          error.message
        )
      }

      @Test
      fun `passes if the order is different`() {
        expectThat(subject)
          .containsExactlyInAnyOrder("rubberplant", "catflap", "marzipan")
      }
    }

    @Nested
    @DisplayName("a non-Collection Iterable subject")
    inner class NonCollection {
      val subject = object : Iterable<String> {
        override fun iterator() =
          arrayOf("catflap", "rubberplant", "marzipan").iterator()
      }

      @Test
      fun `passes if the elements are identical`() {
        expectThat(subject)
          .containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan")
      }

      @Test
      fun `passes if the elements are ordered differently`() {
        expectThat(subject)
          .containsExactlyInAnyOrder("marzipan", "rubberplant", "catflap")
      }

      @Test
      fun `fails if there are more elements than expected`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactlyInAnyOrder("catflap", "rubberplant")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "fnord"
            )
        }
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        assertThrows<AssertionError> {
          expectThat(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "marzipan"
            )
        }
      }

      @Test
      fun `fails if it's supposed to be empty and isn't`() {
        assertThrows<AssertionError> {
          expectThat(subject).containsExactlyInAnyOrder()
        }
      }

      @Test
      fun `passes if it's supposed to be empty and is`() {
        val emptySubject = object : Iterable<String> {
          override fun iterator() = emptySequence<String>().iterator()
        }
        expectThat(emptySubject).containsExactlyInAnyOrder()
      }
    }
  }
}
