package strikt.assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.assertThrows
import strikt.api.expect
import strikt.fails

internal object IterableAssertions : Spek({
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
      }
      it("fails if all elements conform") {
        fails {
          val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
          expect(subject).none {
            isUpperCase()
          }
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
        Pair(emptyList(), arrayOf("catflap"))
      ).forEach { (subject, expected) ->
        it("fails $subject contains ${expected.toList()}") {
          fails {
            expect(subject).contains(*expected)
          }
        }
      }

      it("rejects an empty array of expected elements") {
        assertThrows<IllegalArgumentException> {
          expect(listOf("catflap", "rubberplant", "marzipan")).contains()
        }
      }

      it("has a nested failure for each missing element") {
        fails {
          expect(listOf("catflap", "rubberplant", "marzipan")).contains("covfefe", "marzipan", "bojack")
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
          assertThrows<IllegalArgumentException> {
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
})