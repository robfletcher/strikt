package kirk

import kirk.api.Assertion
import kirk.api.expect
import kirk.assertions.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate

internal object Assertions : Spek({

  describe("assertions on ${Any::class.simpleName}") {
    describe("isNull assertion") {
      it("passes if the subject is null") {
        val subject: Any? = null
        expect(subject).isNull()
      }
      it("fails if the subject is not null") {
        fails {
          val subject: Any? = "covfefe"
          expect(subject).isNull()
        }
      }
      @Suppress("USELESS_IS_CHECK")
      it("down-casts the result") {
        val subject: Any? = null
        expect(subject)
          .also { assert(it is Assertion<Any?>) }
          .isNull()
          .also { assert(it is Assertion<Nothing>) }
      }
    }

    describe("isNotNull assertion") {
      it("fails if the subject is null") {
        fails {
          val subject: Any? = null
          expect(subject).isNotNull()
        }
      }
      it("passes if the subject is not null") {
        val subject: Any? = "covfefe"
        expect(subject).isNotNull()
      }
      @Suppress("USELESS_IS_CHECK")
      it("down-casts the result") {
        val subject: Any? = "covfefe"
        expect(subject)
          .also { assert(it is Assertion<Any?>) }
          .isNotNull()
          .also { assert(it is Assertion<Any>) }
      }
    }

    describe("isA assertion") {
      it("fails if the subject is null") {
        fails {
          val subject: Any? = null
          expect(subject).isA<String>()
        }
      }
      it("fails if the subject is a different type") {
        fails {
          val subject = 1L
          expect(subject).isA<String>()
        }
      }
      it("passes if the subject is the same exact type") {
        val subject = "covfefe"
        expect(subject).isA<String>()
      }
      it("passes if the subject is a sub-type") {
        val subject: Any = 1L
        expect(subject).isA<Number>()
      }
      @Suppress("USELESS_IS_CHECK")
      it("down-casts the result") {
        val subject: Any = 1L
        expect(subject)
          .also { assert(it is Assertion<Any>) }
          .isA<Number>()
          .also { assert(it is Assertion<Number>) }
          .isA<Long>()
          .also { assert(it is Assertion<Long>) }
      }
      @Suppress("USELESS_IS_CHECK")
      it("allows specialized assertions after establishing type") {
        val subject: Any = "covfefe"
        expect(subject)
          .also { assert(it is Assertion<Any>) }
          .isA<String>()
          .also { assert(it is Assertion<String>) }
          .hasLength(7) // only available on Assertion<CharSequence>
      }
    }

    describe("isEqualTo assertion") {
      it("passes if the subject matches the expectation") {
        expect("covfefe").isEqualTo("covfefe")
      }

      sequenceOf(
        Pair("covfefe", "COVFEFE"),
        Pair(1, 1L),
        Pair(null, "covfefe"),
        Pair("covfefe", null)
      ).forEach { (subject, expected) ->
        it("fails $subject is equal to $expected") {
          fails {
            expect(subject).isEqualTo(expected)
          }
        }
      }
    }

    describe("isNotEqualTo assertion") {
      it("fails if the subject matches the expectation") {
        fails {
          expect("covfefe").isNotEqualTo("covfefe")
        }
      }

      sequenceOf(
        Pair("covfefe", "COVFEFE"),
        Pair(1, 1L),
        Pair(null, "covfefe"),
        Pair("covfefe", null)
      ).forEach { (subject, expected) ->
        it("passes $subject is not equal to $expected") {
          expect(subject).isNotEqualTo(expected)
        }
      }
    }
  }

  describe("assertions on ${CharSequence::class.simpleName}") {
    describe("hasLength assertion") {
      it("passes if the subject has the expected length") {
        expect("covfefe").hasLength(7)
      }
      it("fails if the subject does not have the expected length") {
        fails {
          expect("covfefe").hasLength(1)
        }
      }
    }

    describe("matches assertion") {
      it("passes if the subject is a full match for the regex") {
        expect("covfefe").matches("[cefov]+".toRegex())
      }
      it("fails if the subject is only a partial match for the regex") {
        fails {
          expect("despite the negative press covfefe").matches("[cefov]+".toRegex())
        }
      }
      it("fails if the subject does not match the regex") {
        fails {
          expect("covfefe").matches("\\d+".toRegex())
        }
      }
    }
  }

  describe("assertions on ${Collection::class.simpleName}") {
    describe("hasSize assertion") {
      it("fails if the subject size is not the expected size") {
        fails {
          val subject = setOf("catflap", "rubberplant", "marzipan")
          expect(subject).hasSize(1)
        }
      }

      describe("isEmpty assertion") {
        it("passes if collection is empty") {
          expect(emptyList<Any>()).isEmpty()
        }
        it("fails if the collection is not empty") {
          fails {
            expect(listOf("catflap", "rubberplant", "marzipan")).isEmpty()
          }
        }
      }

      describe("isNotEmpty assertion") {
        it("fails if collection is empty") {
          fails {
            expect(emptyList<Any>()).isNotEmpty()
          }
        }
        it("passes if the collection is not empty") {
          expect(listOf("catflap", "rubberplant", "marzipan")).isNotEmpty()
        }
      }
    }
  }

  describe("assertions on ${Iterable::class.simpleName}") {
    describe("all assertion") {
      it("passes if all elements conform") {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject).all {
          isLowerCase()
        }
      }
      it("fails if any element does not conform") {
        fails {
          val subject = setOf("catflap", "rubberplant", "marzipan")
          expect(subject).all {
            startsWith('c')
          }
        }
          .let { failure ->
            assertEquals(4, failure.assertionCount, "Assertions")
            assertEquals(1, failure.passCount, "Passed")
            assertEquals(3, failure.failureCount, "Failed")
          }
      }
    }

    describe("any assertion") {
      it("passes if all elements conform") {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject).any {
          isLowerCase()
        }
      }
      it("passes if any one element conforms") {
        val subject = setOf("catflap", "RUBBERPLANT", "MARZIPAN")
        expect(subject).any {
          isLowerCase()
        }
      }
      it("fails if no elements conform") {
        fails {
          val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
          expect(subject).any {
            isLowerCase()
          }
        }
          .let { failure ->
            assertEquals(4, failure.assertionCount, "Assertions")
            assertEquals(0, failure.passCount, "Passed")
            assertEquals(4, failure.failureCount, "Failed")
          }
      }
    }

    describe("none assertion") {
      it("passes if no elements conform") {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject).none {
          isUpperCase()
        }
      }
      it("fails if some elements conforms") {
        fails {
          val subject = setOf("catflap", "RUBBERPLANT", "MARZIPAN")
          expect(subject).none {
            isUpperCase()
          }
        }
          .let { failure ->
            assertEquals(4, failure.assertionCount, "Assertions")
            assertEquals(3, failure.passCount, "Passed")
            assertEquals(1, failure.failureCount, "Failed")
          }
      }
      it("fails if all elements conform") {
        fails {
          val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
          expect(subject).none {
            isUpperCase()
          }
        }
          .let { failure ->
            assertEquals(4, failure.assertionCount, "Assertions")
            assertEquals(4, failure.passCount, "Passed")
            assertEquals(0, failure.failureCount, "Failed")
          }
      }
    }

    describe("contains assertion") {
      sequenceOf(
        Pair(listOf("catflap"), arrayOf("catflap")),
        Pair(listOf("catflap", "rubberplant", "marzipan"), arrayOf("catflap")),
        Pair(listOf("catflap", "rubberplant", "marzipan"), arrayOf("catflap", "marzipan"))
      ).forEach { (subject, expected) ->
        it("passes $subject contains ${expected.toList()}") {
          expect(subject).contains(*expected)
        }
      }

      sequenceOf(
        Pair(listOf("catflap", "rubberplant", "marzipan"), arrayOf("covfefe")),
        Pair(listOf("catflap", "rubberplant", "marzipan"), arrayOf("catflap", "covfefe")),
        Pair(listOf("catflap", "rubberplant", "marzipan"), emptyArray()),
        Pair(emptyList(), arrayOf("catflap"))
      ).forEach { (subject, expected) ->
        it("fails $subject contains ${expected.toList()}") {
          fails {
            expect(subject).contains(*expected)
          }
        }
      }

      it("has a nested failure for each missing element") {
        fails {
          expect(listOf("catflap", "rubberplant", "marzipan")).contains("covfefe", "marzipan", "bojack")
        }.let { e ->
          assertEquals(3, e.assertionCount, "Assertions")
          assertEquals(1, e.passCount, "Passed")
          assertEquals(2, e.failureCount, "Failed")
        }
      }
    }

    describe("doesNotContain assertion") {
      it("always passes for an empty subject") {
        expect(emptyList<String>()).doesNotContain("catflap", "rubberplant", "marzipan")
      }

      sequenceOf(
        emptyList(),
        listOf("catflap", "rubberplant", "marzipan")
      ).forEach { subject ->
        it("fails for the subject $subject if no elements are specified") {
          fails {
            expect(subject).doesNotContain()
          }
        }
      }

      sequenceOf(
        arrayOf("covfefe"),
        arrayOf("xenocracy", "wye", "exercitation")
      ).forEach { elements ->
        it("passes if the subject contains none of the elements ${elements.toList()}") {
          expect(listOf("catflap", "rubberplant", "marzipan")).doesNotContain(*elements)
        }
      }

      sequenceOf(
        arrayOf("catflap"),
        arrayOf("catflap", "kakistocracy", "impeach"),
        arrayOf("owlbear", "marzipan", "illithid")
      ).forEach { elements ->
        it("passes if the subject contains any of the elements ${elements.toList()}") {
          fails {
            expect(listOf("catflap", "rubberplant", "marzipan")).doesNotContain(*elements)
          }
        }
      }
    }

    describe("containsExactly assertion") {
      given("a Set subject") {
        val subject = setOf("catflap", "rubberplant", "marzipan")

        it("passes if the elements are identical") {
          expect(subject).containsExactly("catflap", "rubberplant", "marzipan")
        }

        it("fails if there are more elements than expected") {
          fails {
            expect(subject).containsExactly("rubberplant", "catflap")
          }
        }

        it("fails if there are fewer elements than expected") {
          fails {
            expect(subject).containsExactly("catflap", "rubberplant", "marzipan", "covfefe")
          }
        }

        it("fails if the order is different (even though this is a Set)") {
          fails {
            expect(subject).containsExactly("rubberplant", "catflap", "marzipan")
          }
        }
      }

      given("a List subject") {
        val subject = listOf("catflap", "rubberplant", "marzipan")

        it("passes if all the elements exist in the same order") {
          expect(subject).containsExactly("catflap", "rubberplant", "marzipan")
        }

        it("fails if there are more elements than expected") {
          fails {
            expect(subject).containsExactly("catflap", "rubberplant")
          }
        }

        it("fails if there are fewer elements than expected") {
          fails {
            expect(subject).containsExactly("catflap", "rubberplant", "marzipan", "covfefe")
          }
        }

        it("fails if the order is different") {
          fails {
            expect(subject).containsExactly("rubberplant", "catflap", "marzipan")
          }
        }

        it("fails if the cardinality of an element is lower than expected") {
          fails {
            expect(subject).containsExactly("catflap", "rubberplant", "marzipan", "marzipan")
          }
        }
      }

      given("a non-Collection Iterable subject") {
        val subject = object : Iterable<String> {
          override fun iterator() =
            arrayOf("catflap", "rubberplant", "marzipan").iterator()
        }

        it("passes if the elements are indentical") {
          expect("a non-Collection iterable %s", subject)
            .containsExactly("catflap", "rubberplant", "marzipan")
        }

        it("fails if the elements are ordered differently") {
          fails {
            expect("a non-Collection iterable %s", subject)
              .containsExactly("marzipan", "rubberplant", "catflap")
          }
        }

        it("fails if there are more elements than expected") {
          fails {
            expect("a non-Collection iterable %s", subject)
              .containsExactly("catflap", "rubberplant")
          }
        }

        it("fails if there are fewer elements than expected") {
          fails {
            expect("a non-Collection iterable %s", subject)
              .containsExactly("catflap", "rubberplant", "marzipan", "covfefe")
          }
        }

        it("fails if the cardinality of an element is lower than expected") {
          fails {
            expect("a non-Collection iterable %s", subject)
              .containsExactly("catflap", "rubberplant", "marzipan", "marzipan")
          }
        }

        it("fails if it's supposed to be empty and isn't") {
          fails {
            expect("a non-Collection iterable %s", subject).containsExactly()
          }
        }

        it("passes if it's supposed to be empty and is") {
          val emptySubject = object : Iterable<String> {
            override fun iterator() = emptySequence<String>().iterator()
          }
          expect(emptySubject).containsExactly()
        }
      }
    }

    describe("containsExactlyInAnyOrder assertion") {
      given("a Set subject") {
        val subject = setOf("catflap", "rubberplant", "marzipan")

        it("passes if the elements are identical") {
          expect(subject).containsExactlyInAnyOrder("rubberplant", "catflap", "marzipan")
        }

        it("fails if there are more elements than expected") {
          fails {
            expect(subject).containsExactlyInAnyOrder("rubberplant", "catflap")
          }
        }

        it("fails if there are fewer elements than expected") {
          fails {
            expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan", "covfefe")
          }
        }
      }

      given("a List subject") {
        val subject = listOf("catflap", "rubberplant", "marzipan")

        it("passes if all the elements exist in the same order") {
          expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan")
        }

        it("fails if there are more elements than expected") {
          fails {
            expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant")
          }
        }

        it("fails if the cardinality of an element is lower than expected") {
          fails {
            expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan", "marzipan")
          }
        }

        it("fails if there are fewer elements than expected") {
          fails {
            expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan", "covfefe")
          }
        }

        it("passes if the order is different") {
          expect(subject).containsExactlyInAnyOrder("rubberplant", "catflap", "marzipan")
        }
      }

      given("a non-Collection Iterable subject") {
        val subject = object : Iterable<String> {
          override fun iterator() =
            arrayOf("catflap", "rubberplant", "marzipan").iterator()
        }

        it("passes if the elements are indentical") {
          expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan")
        }

        it("passes if the elements are ordered differently") {
          expect(subject).containsExactlyInAnyOrder("marzipan", "rubberplant", "catflap")
        }

        it("fails if there are more elements than expected") {
          fails {
            expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant")
          }
        }

        it("fails if there are fewer elements than expected") {
          fails {
            expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan", "covfefe")
          }
        }

        it("fails if the cardinality of an element is lower than expected") {
          fails {
            expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan", "marzipan")
          }
        }

        it("fails if it's supposed to be empty and isn't") {
          fails {
            expect(subject).containsExactlyInAnyOrder()
          }
        }

        it("passes if it's supposed to be empty and is") {
          val emptySubject = object : Iterable<String> {
            override fun iterator() = emptySequence<String>().iterator()
          }
          expect(emptySubject).containsExactlyInAnyOrder()
        }
      }
    }
  }

  describe("assertions on ${Comparable::class.simpleName}") {
    describe("isGreaterThan assertion") {
      it("passes if the subject is greater than the expected value") {
        expect(1).isGreaterThan(0)
      }
      it("fails if the subject is equal to the expected value") {
        fails {
          expect(1).isGreaterThan(1)
        }
      }
      it("fails if the subject is less than the expected value") {
        fails {
          expect(LocalDate.of(2018, 5, 1)).isGreaterThan(LocalDate.of(2018, 5, 2))
        }
      }
    }

    describe("isLessThan assertion") {
      it("passes if the subject is less than the expected value") {
        expect(0).isLessThan(1)
      }
      it("fails if the subject is equal to the expected value") {
        fails {
          expect(1).isLessThan(1)
        }
      }
      it("fails if the subject is greater than the expected value") {
        fails {
          expect(LocalDate.of(2018, 5, 2)).isLessThan(LocalDate.of(2018, 5, 1))
        }
      }
    }
    describe("isGreaterThanOrEqualTo assertion") {
      it("passes if the subject is greater than the expected value") {
        expect(1).isGreaterThanOrEqualTo(0)
      }
      it("passes if the subject is equal to the expected value") {
        expect(1).isGreaterThanOrEqualTo(1)
      }
      it("fails if the subject is less than the expected value") {
        fails {
          expect(LocalDate.of(2018, 5, 1)).isGreaterThanOrEqualTo(LocalDate.of(2018, 5, 2))
        }
      }
    }

    describe("isLessThanOrEqualTo assertion") {
      it("passes if the subject is less than the expected value") {
        expect(0).isLessThanOrEqualTo(1)
      }
      it("passes if the subject is equal to the expected value") {
        expect(1).isLessThanOrEqualTo(1)
      }
      it("fails if the subject is greater than the expected value") {
        fails {
          expect(LocalDate.of(2018, 5, 2)).isLessThanOrEqualTo(LocalDate.of(2018, 5, 1))
        }
      }
    }
  }
})

private fun File.toPathElements(): Array<Path> {
  return path
    .split(File.separator)
    .filterNot { it == "" }
    .map { Paths.get(it) }
    .toTypedArray()
}
