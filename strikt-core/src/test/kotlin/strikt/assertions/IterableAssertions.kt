package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.MultipleFailuresError
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.internal.opentest4j.MappingFailed

internal object IterableAssertions : JUnit5Minutests {
  /**
   * Turns a list subject into various iterable types with the same content.
   */
  private fun <E : Comparable<E>> List<E>.permute(): List<Iterable<E>> =
    listOf(
      this,
      toSet(),
      toSortedSet()
    )

  /**
   * Turns a list subject with expected values into various iterable types with
   * the same content and the same expected value.
   */
  private fun <E : Comparable<E>, EX> List<Pair<List<E>, EX>>.permuteExpected(): List<Pair<Iterable<E>, EX>> =
    flatMap {
      listOf(
        it.first to it.second,
        it.first.toSet() to it.second,
        it.first.toSortedSet() to it.second
      )
    }

  fun tests() =
    rootContext {
      context("all assertion") {
        context("passes if all elements") {
          listOf("catflap", "rubberplant", "marzipan")
            .permute()
            .forEach { subject ->
              test("a ${subject.javaClass.simpleName} conform") {
                expectThat(subject).all {
                  isLowerCase()
                }
              }
            }
        }

        context("fails if any element") {
          listOf("catflap", "rubberplant", "marzipan")
            .permute()
            .forEach { subject ->
              test("of a ${subject.javaClass.simpleName} does not conform") {
                assertThrows<AssertionError> {
                  expectThat(subject).all {
                    startsWith('c')
                  }
                }
              }
            }
        }
      }

      context("allIndexed assertion") {
        context("passes if all elements") {
          test("of a List conform") {
            val subject = listOf("catflap-0", "rubberplant-1", "marzipan-2")
            expectThat(subject).allIndexed { index ->
              endsWith("-$index")
            }
          }
        }

        context("fails if any element") {
          test("of a List does not conform") {
            val subject = listOf("catflap-1", "rubberplant-1", "marzipan-1")
            assertThrows<AssertionError> {
              expectThat(subject).allIndexed { index ->
                endsWith("-$index")
              }
            }
          }
        }
      }

      context("any assertion") {
        context("passes if") {
          listOf("catflap", "rubberplant", "marzipan")
            .permute()
            .forEach { subject ->
              test("all elements of a ${subject.javaClass.simpleName} conform") {
                expectThat(subject).any {
                  isLowerCase()
                }
              }
            }

          listOf("catflap", "RUBBERPLANT", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("one element of a ${subject.javaClass.simpleName} conforms") {
                expectThat(subject).any {
                  isLowerCase()
                }
              }
            }
        }

        context("fails if") {
          listOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("no elements of a ${subject.javaClass.simpleName} conform") {
                assertThrows<AssertionError> {
                  expectThat(subject).any {
                    isLowerCase()
                  }
                }
              }
            }
        }

        test("works with not") {
          val subject = setOf("catflap", "rubberplant", "marzipan")
          expectThat(subject).not().any {
            isUpperCase()
          }
        }
      }

      context("anyIndexed assertion") {
        context("passes if") {
          test("all elements of a List conform") {
            val subject = listOf("catflap-0", "rubberplant-1", "marzipan-2")
            expectThat(subject).anyIndexed { index ->
              endsWith("-$index")
            }
          }

          test("one element of a List conforms") {
            val subject = listOf("catflap-1", "rubberplant-1", "marzipan-1")
            expectThat(subject).anyIndexed { index ->
              endsWith("-$index")
            }
          }
        }

        context("fails if") {
          test("no elements of a List conform") {
            val subject = listOf("catflap", "rubberplant", "marzipan")
            assertThrows<AssertionError> {
              expectThat(subject).anyIndexed { index ->
                endsWith("-$index")
              }
            }
          }
        }

        test("works with not") {
          val subject = setOf("catflap", "rubberplant", "marzipan")
          expectThat(subject).not().anyIndexed { index ->
            endsWith("-$index")
          }
        }
      }

      context("none assertion") {
        context("passes if") {
          listOf("catflap", "rubberplant", "marzipan")
            .permute()
            .forEach { subject ->
              test("no elements of a ${subject.javaClass.simpleName} conform") {
                expectThat(subject).none {
                  isUpperCase()
                }
              }
            }
        }

        context("fails if") {
          listOf("catflap", "RUBBERPLANT", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("some elements of a ${subject.javaClass.simpleName} conforms") {
                assertThrows<AssertionError> {
                  expectThat(subject).none {
                    isUpperCase()
                  }
                }
              }
            }

          listOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("all elements of a ${subject.javaClass.simpleName} conform") {
                assertThrows<AssertionError> {
                  expectThat(subject).none {
                    isUpperCase()
                  }
                }
              }
            }

          test("works with not") {
            val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
            expectThat(subject).not().none {
              isUpperCase()
            }
          }
        }
      }

      context("noneIndexed assertion") {
        context("passes if") {
          test("no elements of a List conform") {
            val subject = listOf("catflap", "rubberplant", "marzipan")
            expectThat(subject).noneIndexed { index ->
              endsWith("-$index")
            }
          }
        }

        context("fails if") {
          test("some elements of a List conforms") {
            val subject = listOf("catflap-1", "rubberplant-1", "marzipan-1")
            assertThrows<AssertionError> {
              expectThat(subject).noneIndexed { index ->
                endsWith("-$index")
              }
            }
          }

          test("all elements of a List conform") {
            val subject = listOf("catflap-0", "rubberplant-1", "marzipan-2")
            assertThrows<AssertionError> {
              expectThat(subject).noneIndexed { index ->
                endsWith("-$index")
              }
            }
          }

          test("works with not") {
            val subject = setOf("catflap-1", "rubberplant-1", "marzipan-1")
            expectThat(subject).not().noneIndexed { index ->
              endsWith("-$index")
            }
          }
        }
      }

      context("at least assertion") {
        context("fails if") {
          listOf("catflap", "rubberplant", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("not enough elements of a ${subject.javaClass.simpleName} conform") {
                assertThrows<AssertionError> {
                  expectThat(subject).atLeast(2) {
                    isUpperCase()
                  }
                }
              }
            }
        }

        context("passes if") {
          listOf("catflap", "RUBBERPLANT", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("exactly minimum amount of elements of a ${subject.javaClass.simpleName} conforms") {
                expectThat(subject).atLeast(2) {
                  isUpperCase()
                }
              }
            }

          listOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("all elements of a ${subject.javaClass.simpleName} conform") {
                expectThat(subject).atLeast(2) {
                  isUpperCase()
                }
              }
            }
        }

        test("works with not") {
          val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
          assertThrows<AssertionError> {
            expectThat(subject).not().atLeast(2) {
              isUpperCase()
            }
          }
        }
      }

      context("at most assertion") {
        context("passes if") {
          listOf("catflap", "rubberplant", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("fewer elements of a ${subject.javaClass.simpleName} conform") {
                expectThat(subject).atMost(2) {
                  isUpperCase()
                }
              }
            }

          listOf("catflap", "RUBBERPLANT", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("exactly maximum amount of elements of a ${subject.javaClass.simpleName} conforms") {
                expectThat(subject).atMost(2) {
                  isUpperCase()
                }
              }
            }
        }

        context("fails if") {
          listOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("too many elements of a ${subject.javaClass.simpleName} conform") {
                assertThrows<AssertionError> {
                  expectThat(subject).atMost(2) {
                    isUpperCase()
                  }
                }
              }
            }
        }

        test("works with not") {
          val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
          expectThat(subject).not().atMost(2) {
            isUpperCase()
          }
        }
      }

      context("one assertion") {
        context("passes if") {
          listOf("catflap", "rubberplant", "MARZIPAN")
            .permute()
            .forEach { subject ->
              test("exactly one element of a ${subject.javaClass.simpleName} conforms") {
                expectThat(subject).one {
                  isUpperCase()
                }
              }
            }
        }

        context("fails if") {
          listOf("CATFLAP", "RUBBERPLANT", "marzipan")
            .permute()
            .forEach { subject ->
              test("too many elements of a ${subject.javaClass.simpleName} conform") {
                assertThrows<AssertionError> {
                  expectThat(subject).one {
                    isUpperCase()
                  }
                }
              }
            }

          listOf("catflap", "rubberplant", "marzipan")
            .permute()
            .forEach { subject ->
              test("no elements of a ${subject.javaClass.simpleName} conform") {
                assertThrows<AssertionError> {
                  expectThat(subject).one {
                    isUpperCase()
                  }
                }
              }
            }
        }

        test("works with not") {
          val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
          expectThat(subject).not().one {
            isUpperCase()
          }
        }
      }

      context("contains assertion") {
        context("passes if") {
          listOf(
            listOf("catflap") to arrayOf("catflap"),
            listOf("catflap", "rubberplant", "marzipan") to arrayOf("catflap"),
            listOf("catflap", "rubberplant", "marzipan") to
              arrayOf(
                "catflap",
                "marzipan"
              )
          ).permuteExpected()
            .forEach { (subject, expected) ->
              test("$subject (${subject.javaClass.simpleName}) contains ${expected.toList()}") {
                expectThat(subject).contains(*expected)
              }
            }
        }

        context("fails if") {
          listOf(
            listOf("catflap", "rubberplant", "marzipan") to arrayOf("fnord"),
            listOf("catflap", "rubberplant", "marzipan") to
              arrayOf(
                "catflap",
                "fnord"
              ),
            emptyList<String>() to arrayOf("catflap")
          ).permuteExpected()
            .forEach { (subject, expected) ->
              test("$subject (${subject.javaClass.simpleName}) does not contain ${expected.toList()}") {
                assertThrows<AssertionError> {
                  expectThat(subject).contains(*expected)
                }
              }
            }
        }

        test("any collection contains an empty list") {
          expectThat(listOf("catflap", "rubberplant", "marzipan")).contains()
        }

        test("an empty collection contains an empty list") {
          expectThat(emptyList<Any>()).contains()
        }

        test("has a nested failure for each missing element when there are multiple") {
          val error =
            assertThrows<AssertionError> {
              expectThat(listOf("catflap", "rubberplant", "marzipan"))
                .contains("fnord", "marzipan", "bojack")
            }
          expectThat(error.message).isEqualTo(
            """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
          |  ✗ contains the elements ["fnord", "marzipan", "bojack"]
          |    ✗ contains "fnord"
          |    ✓ contains "marzipan"
          |    ✗ contains "bojack"
            """.trimMargin()
          )
        }

        test("does not nest failures when there is only one element") {
          val error =
            assertThrows<AssertionError> {
              expectThat(listOf("catflap", "rubberplant", "marzipan"))
                .contains("fnord")
            }
          expectThat(error.message).isEqualTo(
            "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
              "  ✗ contains \"fnord\""
          )
        }
      }

      context("doesNotContain assertion") {
        context("passes if") {
          emptyList<String>()
            .permute()
            .forEach { subject ->
              test("subject is empty ${subject.javaClass.simpleName}") {
                expectThat(subject)
                  .doesNotContain("catflap", "rubberplant", "marzipan")
              }
            }

          listOf(
            listOf("catflap", "rubberplant", "marzipan") to arrayOf("fnord"),
            listOf("catflap", "rubberplant", "marzipan") to
              arrayOf(
                "xenocracy",
                "wye",
                "exercitation"
              )
          ).permuteExpected()
            .forEach { (subject, elements) ->
              test("a ${subject.javaClass.simpleName} contains none of the elements ${elements.toList()}") {
                expectThat(subject)
                  .doesNotContain(*elements)
              }
            }
        }

        context("fails if") {
          listOf(
            emptyList(),
            listOf("catflap", "rubberplant", "marzipan")
          ).flatMap { it.permute() }
            .forEach { subject ->
              test("no elements are specified for subject $subject") {
                assertThrows<IllegalArgumentException> {
                  expectThat(subject).doesNotContain()
                }
              }
            }

          listOf(
            listOf("catflap", "rubberplant", "marzipan") to arrayOf("catflap"),
            listOf("catflap", "rubberplant", "marzipan") to
              arrayOf(
                "catflap",
                "kakistocracy",
                "impeach"
              ),
            listOf("catflap", "rubberplant", "marzipan") to
              arrayOf(
                "owlbear",
                "marzipan",
                "illithid"
              )
          )
            .permuteExpected()
            .forEach { (subject, elements) ->
              test("a ${subject.javaClass.simpleName} contains any of the elements ${elements.toList()}") {
                assertThrows<AssertionError> {
                  expectThat(subject)
                    .doesNotContain(*elements)
                }
              }
            }
        }

        test("formats its failure message correctly when there are multiple elements") {
          val error =
            assertThrows<AssertionError> {
              expectThat(listOf("catflap", "rubberplant", "marzipan"))
                .doesNotContain("catflap", "wye", "marzipan")
            }
          expectThat(error.message).isEqualTo(
            """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
          |  ✗ does not contain any of the elements ["catflap", "wye", "marzipan"]
          |    ✗ does not contain "catflap"
          |    ✓ does not contain "wye"
          |    ✗ does not contain "marzipan"
            """.trimMargin()
          )
        }

        test("formats its failure message correctly when there is a single element") {
          val error =
            assertThrows<AssertionError> {
              expectThat(listOf("catflap", "rubberplant", "marzipan"))
                .doesNotContain("catflap")
            }
          expectThat(error.message).isEqualTo(
            "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
              "  ✗ does not contain \"catflap\""
          )
        }
      }

      context("containsExactly assertion") {
        derivedContext<Set<String>>("for ${Set::class}") {
          fixture { setOf("catflap", "rubberplant", "marzipan") }
          test("passes if the elements are identical") {
            expectThat(fixture)
              .containsExactly("catflap", "rubberplant", "marzipan")
          }

          test("fails if there are more elements than expected") {
            assertThrows<AssertionError> {
              expectThat(fixture).containsExactly("rubberplant", "catflap")
            }
          }

          test("fails if there are fewer elements than expected") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
            }
          }

          test("fails if the order is different (even though this is a Set)") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .containsExactly("rubberplant", "catflap", "marzipan")
            }
          }
        }

        derivedContext<List<String>>("for ${List::class}") {
          fixture { listOf("catflap", "rubberplant", "marzipan") }

          test("passes if all the elements exist in the same order") {
            expectThat(fixture)
              .containsExactly("catflap", "rubberplant", "marzipan")
          }

          test("fails if there are more elements than expected") {
            val error =
              assertThrows<AssertionError> {
                expectThat(fixture).containsExactly("catflap", "rubberplant")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant"]
            |    ✓ contains "catflap"
            |    ✓ …at index 0
            |    ✓ contains "rubberplant"
            |    ✓ …at index 1
            |    ✗ contains no further elements
            |      found ["marzipan"]
              """.trimMargin()
            )
          }

          test("fails if there are fewer elements than expected") {
            val error =
              assertThrows<AssertionError> {
                expectThat(fixture)
                  .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant", "marzipan", "fnord"]
            |    ✓ contains "catflap"
            |    ✓ …at index 0
            |    ✓ contains "rubberplant"
            |    ✓ …at index 1
            |    ✓ contains "marzipan"
            |    ✓ …at index 2
            |    ✗ contains "fnord"
            |    ✓ contains no further elements
              """.trimMargin()
            )
          }

          /**
           * @see https://github.com/robfletcher/strikt/issues/159
           */
          test("fails if there are fewer elements than expected but the outlier is in the actual list") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .containsExactly("fnord", "catflap", "rubberplant", "marzipan")
            }
          }

          test("fails if the order is different") {
            val error =
              assertThrows<AssertionError> {
                expectThat(fixture)
                  .containsExactly("rubberplant", "catflap", "marzipan")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["rubberplant", "catflap", "marzipan"]
            |    ✓ contains "rubberplant"
            |    ✗ …at index 0
            |      found "catflap"
            |    ✓ contains "catflap"
            |    ✗ …at index 1
            |      found "rubberplant"
            |    ✓ contains "marzipan"
            |    ✓ …at index 2
            |    ✓ contains no further elements
              """.trimMargin()
            )
          }

          test("fails if the cardinality of an element is lower than expected") {
            val error =
              assertThrows<AssertionError> {
                expectThat(fixture)
                  .containsExactly("catflap", "rubberplant", "marzipan", "marzipan")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant", "marzipan", "marzipan"]
            |    ✓ contains "catflap"
            |    ✓ …at index 0
            |    ✓ contains "rubberplant"
            |    ✓ …at index 1
            |    ✓ contains "marzipan"
            |    ✓ …at index 2
            |    ✗ contains "marzipan"
            |    ✓ contains no further elements
              """.trimMargin()
            )
          }
        }

        derivedContext<Iterable<String>>("a non-Collection Iterable subject") {
          fixture {
            object : Iterable<String> {
              override fun iterator() = arrayOf("catflap", "rubberplant", "marzipan").iterator()
            }
          }

          test("passes if the elements are indentical") {
            expectThat(fixture)
              .describedAs("a non-Collection iterable %s")
              .containsExactly("catflap", "rubberplant", "marzipan")
          }

          test("fails if the elements are ordered differently") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .describedAs("a non-Collection iterable %s")
                .containsExactly("marzipan", "rubberplant", "catflap")
            }
          }

          test("fails if there are more elements than expected") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .describedAs("a non-Collection iterable %s")
                .containsExactly("catflap", "rubberplant")
            }
          }

          test("fails if there are fewer elements than expected") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .describedAs("a non-Collection iterable %s")
                .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
            }
          }

          test("fails if the cardinality of an element is lower than expected") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .describedAs("a non-Collection iterable %s")
                .containsExactly("catflap", "rubberplant", "marzipan", "marzipan")
            }
          }

          test("fails if it's supposed to be empty and isn't") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .describedAs("a non-Collection iterable %s")
                .containsExactly()
            }
          }

          test("passes if it's supposed to be empty and is") {
            val emptySubject =
              object : Iterable<String> {
                override fun iterator() = emptySequence<String>().iterator()
              }
            expectThat(emptySubject).containsExactly()
          }
        }
      }

      context("containsExactlyInAnyOrder assertion") {
        derivedContext<Set<String>>("a ${Set::class}") {
          fixture { setOf("catflap", "rubberplant", "marzipan") }

          test("passes if the elements are identical") {
            expectThat(fixture)
              .containsExactlyInAnyOrder("rubberplant", "catflap", "marzipan")
          }

          test("fails if there are more elements than expected") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .containsExactlyInAnyOrder("rubberplant", "catflap")
            }
          }

          test("fails if there are fewer elements than expected") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .containsExactlyInAnyOrder(
                  "catflap",
                  "rubberplant",
                  "marzipan",
                  "fnord"
                )
            }
          }
        }

        derivedContext<List<String>>("a ${List::class}") {
          fixture { listOf("catflap", "rubberplant", "marzipan") }

          test("passes if all the elements exist in the same order") {
            expectThat(fixture)
              .containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan")
          }

          test("fails if there are more elements than expected") {
            val error =
              assertThrows<AssertionError> {
                expectThat(fixture)
                  .containsExactlyInAnyOrder("catflap", "rubberplant")
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant"] in any order
            |    ✓ contains "catflap"
            |    ✓ contains "rubberplant"
            |    ✗ contains no further elements
            |      found ["marzipan"]
              """.trimMargin()
            )
          }

          test("fails if the cardinality of an element is lower than expected") {
            val error =
              assertThrows<AssertionError> {
                expectThat(fixture)
                  .containsExactlyInAnyOrder(
                    "catflap",
                    "rubberplant",
                    "marzipan",
                    "marzipan"
                  )
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant", "marzipan", "marzipan"] in any order
            |    ✓ contains "catflap"
            |    ✓ contains "rubberplant"
            |    ✓ contains "marzipan"
            |    ✗ contains "marzipan"
            |    ✓ contains no further elements
              """.trimMargin()
            )
          }

          test("fails if there are fewer elements than expected") {
            val error =
              assertThrows<AssertionError> {
                expectThat(fixture)
                  .containsExactlyInAnyOrder(
                    "catflap",
                    "rubberplant",
                    "marzipan",
                    "fnord"
                  )
              }
            expectThat(error.message).isEqualTo(
              """▼ Expect that ["catflap", "rubberplant", "marzipan"]:
            |  ✗ contains exactly the elements ["catflap", "rubberplant", "marzipan", "fnord"] in any order
            |    ✓ contains "catflap"
            |    ✓ contains "rubberplant"
            |    ✓ contains "marzipan"
            |    ✗ contains "fnord"
            |    ✓ contains no further elements
              """.trimMargin()
            )
          }

          test("passes if the order is different") {
            expectThat(fixture)
              .containsExactlyInAnyOrder("rubberplant", "catflap", "marzipan")
          }
        }

        derivedContext<Iterable<String>>("a non-Collection Iterable subject") {
          fixture {
            object : Iterable<String> {
              override fun iterator() = arrayOf("catflap", "rubberplant", "marzipan").iterator()
            }
          }

          test("passes if the elements are identical") {
            expectThat(fixture)
              .containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan")
          }

          test("passes if the elements are ordered differently") {
            expectThat(fixture)
              .containsExactlyInAnyOrder("marzipan", "rubberplant", "catflap")
          }

          test("fails if there are more elements than expected") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .containsExactlyInAnyOrder("catflap", "rubberplant")
            }
          }

          test("fails if there are fewer elements than expected") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .containsExactlyInAnyOrder(
                  "catflap",
                  "rubberplant",
                  "marzipan",
                  "fnord"
                )
            }
          }

          test("fails if the cardinality of an element is lower than expected") {
            assertThrows<AssertionError> {
              expectThat(fixture)
                .containsExactlyInAnyOrder(
                  "catflap",
                  "rubberplant",
                  "marzipan",
                  "marzipan"
                )
            }
          }

          test("fails if it's supposed to be empty and isn't") {
            assertThrows<AssertionError> {
              expectThat(fixture).containsExactlyInAnyOrder()
            }
          }

          test("passes if it's supposed to be empty and is") {
            val emptySubject =
              object : Iterable<String> {
                override fun iterator() = emptySequence<String>().iterator()
              }
            expectThat(emptySubject).containsExactlyInAnyOrder()
          }
        }
      }

      context("withFirst function") {
        listOf("catflap", "rubberplant", "marzipan")
          .permute()
          .forEach { subject ->
            test("runs assertions on the first element of a ${subject.javaClass.simpleName}") {
              expectThat(subject).withFirst {
                isEqualTo("catflap")
              }
            }

            test("fails if the nested assertions fail on the first element of a ${subject.javaClass.simpleName}") {
              assertThrows<MultipleFailuresError> {
                expectThat(subject).withFirst {
                  isEqualTo("rubberplant")
                }
              }
            }
          }
      }

      context("withFirst(predicate) function") {
        listOf("catflap", "rubberplant", "marzipan")
          .permute()
          .forEach { subject ->
            test("runs assertions on the first element of a ${subject.javaClass.simpleName} that matches the predicate") {
              expectThat(subject).withFirst({ it.startsWith('r') }) {
                isEqualTo("rubberplant")
              }
            }

            test(
              "fails if the nested assertions fail on the first element of a ${subject.javaClass.simpleName} that matches the predicate"
            ) {
              assertThrows<MultipleFailuresError> {
                expectThat(subject).withFirst({ it.startsWith('r') }) {
                  isEqualTo("catflap")
                }
              }
            }

            test("fails if nothing in a ${subject.javaClass.simpleName} matches the predicate") {
              assertThrows<MappingFailed> {
                expectThat(subject).withFirst({ it.startsWith('z') }) {
                  isEqualTo("catflap")
                }
              }
            }
          }
      }

      context("withLast function") {
        listOf("catflap", "rubberplant", "marzipan")
          .permute()
          .forEach { subject ->
            test("runs assertions on the last element of a ${subject.javaClass.simpleName}") {
              expectThat(subject.sorted()).withLast {
                isEqualTo("rubberplant")
              }
            }

            test("fails if the nested assertions fail on the last element of a ${subject.javaClass.simpleName}") {
              assertThrows<MultipleFailuresError> {
                expectThat(subject.sorted()).withLast {
                  isEqualTo("marzipan")
                }
              }
            }
          }
      }

      context("withElementAt function") {
        listOf("catflap", "rubberplant", "marzipan")
          .permute()
          .forEach { subject ->
            test("runs assertions on the nth element of a ${subject.javaClass.simpleName}") {
              expectThat(subject.sorted()).withElementAt(1) {
                isEqualTo("marzipan")
              }
            }

            test("fails if the nested assertions fail on the nth element of a ${subject.javaClass.simpleName}") {
              assertThrows<MultipleFailuresError> {
                expectThat(subject.sorted()).withElementAt(1) {
                  isEqualTo("catflap")
                }
              }
            }
          }
      }

      context("withSingle function") {
        test("runs assertions on the single element of a collection") {
          expectThat(listOf("catflap")).withSingle {
            isEqualTo("catflap")
          }
        }

        test("fails if the nested assertions fail on the single element of a collection") {
          assertThrows<MultipleFailuresError> {
            expectThat(listOf("catflap")).withSingle {
              isEqualTo("rubberplant")
            }
          }
        }

        test("fails if a collection is empty") {
          assertThrows<MappingFailed> {
            expectThat(emptyList<String>()).withSingle {
              isEqualTo("rubberplant")
            }
          }
        }

        test("fails if a collection contains more than one element") {
          assertThrows<MappingFailed> {
            expectThat(listOf("catflap", "rubberplant", "marzipan")).withSingle {
              isEqualTo("rubberplant")
            }
          }
        }
      }

      context("count mapping") {
        test("maps to an assertion on the iterable's size") {
          val subject = listOf("catflap", "rubberplant", "marzipan")
          expectThat(subject).count().isEqualTo(subject.size)
        }

        test("maps to an assertion on the count of elements matching the predicate") {
          val subject = listOf("catflap", "rubberplant", "marzipan")
          expectThat(subject)
            .count("elements containing 't'") { it.contains("t") }
            .isEqualTo(2)
        }
      }

      context("isSorted(Comparable) assertion") {
        test("passes if the subject is empty") {
          expectThat(emptyList<Any?>())
            .isSorted(Comparator.comparingInt { it.hashCode() })
        }

        test("fails in a block assertion if the subject is not in order") {
          assertThrows<AssertionError> {
            expectThat(listOf(1, 3, 2)) {
              isSorted(Comparator.naturalOrder())
            }
          }
        }

        context("fails in a chained assertion if the subject is not in order") {
          assertThrows<AssertionError> {
            expectThat(listOf(1, 3, 2))
              .isSorted(Comparator.naturalOrder())
          }
        }

        test("the assertion passes if the subject is in order") {
          expectThat(listOf("catflap", "marzipan", "rubberplant"))
            .isSorted(Comparator.naturalOrder<String>())
        }

        test("fails if the subject is not sorted according to the comparator") {
          assertThrows<AssertionError> {
            expectThat(listOf("catflap", "rubberplant", "marzipan"))
              .isSorted(Comparator.naturalOrder<String>().reversed())
          }
        }
      }

      test("the assertion passes if the Null value is handled through the Comparator instance") {
        expectThat(listOf("catflap", "marzipan", null))
          .isSorted(Comparator.nullsLast(Comparator.naturalOrder<String>()))
      }

      test("fails with NPE if the Null value isn't handled through the Comparator instances") {
        assertThrows<NullPointerException> {
          expectThat(listOf("catflap", "marzipan", null))
            .isSorted(Comparator.naturalOrder<String>())
        }
      }

      derivedContext<Assertion.Builder<Collection<Collection<String>?>>>("isSorted on non-Comparable") {
        context("a non-empty non Comparable value collection subject") {
          fixture {
            expectThat(
              listOf(
                listOf("catflap"),
                listOf("marzipan", "persipan"),
                listOf("rubberplant", "rubber bush", "rubber tree")
              )
            )
          }

          test("the assertion passes") {
            isSorted(Comparator.comparing { it?.size ?: 0 })
          }

          test("fails if the subject is not sorted according to the comparator") {
            assertThrows<AssertionError> {
              isSorted(Comparator.comparing(Collection<String>::size).reversed())
            }
          }
        }
      }

      context("isSorted assertion") {
        test("passes if the subject is empty") {
          expectThat(emptyList<String>()).isSorted()
        }

        test("the assertion passes if the elements are in natural order") {
          expectThat(listOf("catflap", "marzipan", "rubberplant"))
            .isSorted()
        }

        test("the assertion fails if the elements are not in natural order") {
          assertThrows<AssertionError> {
            expectThat(listOf("catflap", "rubberplant", "marzipan"))
              .isSorted()
          }
        }
      }
    }
}
