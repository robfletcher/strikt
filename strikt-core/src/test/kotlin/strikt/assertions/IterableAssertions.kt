package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.fails

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
            fails {
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
            fails {
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
            fails {
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
            fails {
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
  @DisplayName("contains assertion")
  inner class Contains {
    @TestFactory
    fun `passes subject contains expected`() =
      listOf(
        listOf("catflap") to arrayOf("catflap"),
        listOf("catflap", "rubberplant", "marzipan") to arrayOf("catflap"),
        listOf("catflap", "rubberplant", "marzipan") to arrayOf("catflap", "marzipan")
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
            fails {
              expectThat(subject).contains(*expected)
            }
          }
        }

    @Test
    fun `rejects an empty array of expected elements`() {
      assertThrows<IllegalArgumentException> {
        expectThat(listOf("catflap", "rubberplant", "marzipan")).contains()
      }
    }

    @Test
    fun `has a nested failure for each missing element when there are multiple`() {
      val error = fails {
        expectThat(listOf("catflap", "rubberplant", "marzipan"))
          .contains("fnord", "marzipan", "bojack")
      }
      assertEquals(
        "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
          "  ✗ contains the elements [\"fnord\", \"marzipan\", \"bojack\"]\n" +
          "    ✗ contains \"fnord\"\n" +
          "    ✓ contains \"marzipan\"\n" +
          "    ✗ contains \"bojack\"",
        error.message
      )
    }

    @Test
    fun `does not nest failures when there is only one element`() {
      val error = fails {
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
        listOf("catflap", "rubberplant", "marzipan") to arrayOf("xenocracy", "wye", "exercitation")
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
        listOf("catflap", "rubberplant", "marzipan") to arrayOf("catflap", "kakistocracy", "impeach"),
        listOf("catflap", "rubberplant", "marzipan") to arrayOf("owlbear", "marzipan", "illithid")
      )
        .permuteExpected()
        .map { (subject, elements) ->
          dynamicTest("passes if a ${subject.javaClass.simpleName} contains any of the elements ${elements.toList()}") {
            fails {
              expectThat(subject)
                .doesNotContain(*elements)
            }
          }
        }

    @Test
    fun `formats its failure message correctly when there are multiple elements`() {
      val error = fails {
        expectThat(listOf("catflap", "rubberplant", "marzipan"))
          .doesNotContain("catflap", "wye", "marzipan")
      }
      assertEquals(
        "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
          "  ✗ does not contain any of the elements [\"catflap\", \"wye\", \"marzipan\"]\n" +
          "    ✗ does not contain \"catflap\"\n" +
          "    ✓ does not contain \"wye\"\n" +
          "    ✗ does not contain \"marzipan\"",
        error.message
      )
    }

    @Test
    fun `formats its failure message correctly when there is a single element`() {
      val error = fails {
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
        fails {
          expectThat(subject).containsExactly("rubberplant", "catflap")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        fails {
          expectThat(subject)
            .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
        }
      }

      @Test
      fun `fails if the order is different (even though this is a Set)`() {
        fails {
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
        val error = fails {
          expectThat(subject).containsExactly("catflap", "rubberplant")
        }
        assertEquals(
          "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
            "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\"]\n" +
            "    ✓ contains \"catflap\"\n" +
            "    ✓ …at index 0\n" +
            "    ✓ contains \"rubberplant\"\n" +
            "    ✓ …at index 1\n" +
            "    ✗ contains no further elements : found [\"marzipan\"]",
          error.message
        )
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        val error = fails {
          expectThat(subject)
            .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
        }
        assertEquals(
          "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
            "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\", \"marzipan\"…]\n" +
            "    ✓ contains \"catflap\"\n" +
            "    ✓ …at index 0\n" +
            "    ✓ contains \"rubberplant\"\n" +
            "    ✓ …at index 1\n" +
            "    ✓ contains \"marzipan\"\n" +
            "    ✓ …at index 2\n" +
            "    ✗ contains \"fnord\"\n" +
            "    ✓ contains no further elements",
          error.message
        )
      }

      @Test
      fun `fails if the order is different`() {
        val error = fails {
          expectThat(subject)
            .containsExactly("rubberplant", "catflap", "marzipan")
        }
        assertEquals(
          "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
            "  ✗ contains exactly the elements [\"rubberplant\", \"catflap\", \"marzipan\"]\n" +
            "    ✓ contains \"rubberplant\"\n" +
            "    ✗ …at index 0 : found \"catflap\"\n" +
            "    ✓ contains \"catflap\"\n" +
            "    ✗ …at index 1 : found \"rubberplant\"\n" +
            "    ✓ contains \"marzipan\"\n" +
            "    ✓ …at index 2\n" +
            "    ✓ contains no further elements",
          error.message
        )
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        val error = fails {
          expectThat(subject)
            .containsExactly("catflap", "rubberplant", "marzipan", "marzipan")
        }
        assertEquals(
          "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
            "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\", \"marzipan\"…]\n" +
            "    ✓ contains \"catflap\"\n" +
            "    ✓ …at index 0\n" +
            "    ✓ contains \"rubberplant\"\n" +
            "    ✓ …at index 1\n" +
            "    ✓ contains \"marzipan\"\n" +
            "    ✓ …at index 2\n" +
            "    ✗ contains \"marzipan\"\n" +
            "    ✓ contains no further elements",
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
        fails {
          expectThat(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("marzipan", "rubberplant", "catflap")
        }
      }

      @Test
      fun `fails if there are more elements than expected`() {
        fails {
          expectThat(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("catflap", "rubberplant")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        fails {
          expectThat(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
        }
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        fails {
          expectThat(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("catflap", "rubberplant", "marzipan", "marzipan")
        }
      }

      @Test
      fun `fails if it's supposed to be empty and isn't`() {
        fails {
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
        fails {
          expectThat(subject)
            .containsExactlyInAnyOrder("rubberplant", "catflap")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        fails {
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
        val error = fails {
          expectThat(subject)
            .containsExactlyInAnyOrder("catflap", "rubberplant")
        }
        assertEquals(
          "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
            "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\"] in any order\n" +
            "    ✓ contains \"catflap\"\n" +
            "    ✓ contains \"rubberplant\"\n" +
            "    ✗ contains no further elements : found [\"marzipan\"]",
          error.message
        )
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        val error = fails {
          expectThat(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "marzipan"
            )
        }
        assertEquals(
          "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
            "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\", \"marzipan\"…] in any order\n" +
            "    ✓ contains \"catflap\"\n" +
            "    ✓ contains \"rubberplant\"\n" +
            "    ✓ contains \"marzipan\"\n" +
            "    ✗ contains \"marzipan\"\n" +
            "    ✓ contains no further elements",
          error.message
        )
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        val error = fails {
          expectThat(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "fnord"
            )
        }
        assertEquals(
          "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
            "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\", \"marzipan\"…] in any order\n" +
            "    ✓ contains \"catflap\"\n" +
            "    ✓ contains \"rubberplant\"\n" +
            "    ✓ contains \"marzipan\"\n" +
            "    ✗ contains \"fnord\"\n" +
            "    ✓ contains no further elements",
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
        fails {
          expectThat(subject)
            .containsExactlyInAnyOrder("catflap", "rubberplant")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        fails {
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
        fails {
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
        fails {
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
