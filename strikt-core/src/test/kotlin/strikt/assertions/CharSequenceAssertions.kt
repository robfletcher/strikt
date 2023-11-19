package strikt.assertions

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.expectThat

@DisplayName("assertions on CharSequence")
internal object CharSequenceAssertions : JUnit5Minutests {
  fun tests() =
    rootContext<Assertion.Builder<CharSequence>> {
      fixture { expectThat("fnord") }

      context("hasLength") {
        test("passes when the subject has the expected length") {
          hasLength(5)
        }

        test("fails when the subject does not have the expected length") {
          assertThrows<AssertionFailedError> {
            hasLength(1)
          }
        }
      }

      context("matches") {
        test("passes when the subject is a full match for the regex") {
          matches("[dfnor]+".toRegex())
        }

        test("fails when the subject is only a partial match for the regex") {
          assertThrows<AssertionFailedError> {
            matches("[fn]+".toRegex())
          }
        }

        test("fails when the subject is a case insensitive match for the regex") {
          assertThrows<AssertionFailedError> {
            matches("[DFNOR]+".toRegex())
          }
        }

        test("fails when the subject does not match the regex") {
          assertThrows<AssertionFailedError> {
            matches("\\d+".toRegex())
          }
        }
      }

      context("matchesIgnoringCase") {
        test("passes when the subject is a full match for the regex") {
          matchesIgnoringCase("[dfnor]+".toRegex())
        }

        test("fails when the subject is only a partial match for the regex") {
          assertThrows<AssertionFailedError> {
            matchesIgnoringCase("[fn]+".toRegex())
          }
        }

        test("passes when the subject is a case insensitive match for the regex") {
          matchesIgnoringCase("[DFNOR]+".toRegex())
        }

        test("fails when the subject does not match the regex") {
          assertThrows<AssertionFailedError> {
            matchesIgnoringCase("\\d+".toRegex())
          }
        }
      }

      context("contains(Regex)") {
        test("passes when the subject is a full match for the regex") {
          contains("[dfnor]+".toRegex())
        }

        test("passes when the subject is only a partial match for the regex") {
          contains("[fn]+".toRegex())
        }

        test("fails when the subject contains a match with a different case") {
          assertThrows<AssertionFailedError> {
            contains("[DFNOR]+".toRegex())
          }
        }

        test("fails when the subject does not match the regex") {
          assertThrows<AssertionFailedError> {
            contains("\\d+".toRegex())
          }
        }
      }

      context("containsIgnoringCase(Regex)") {
        test("passes when the subject is a full match for the regex") {
          containsIgnoringCase("[dfnor]+".toRegex())
        }

        test("passes when the subject is only a partial match for the regex") {
          containsIgnoringCase("[fn]+".toRegex())
        }

        test("passes when the subject contains a match with a different case") {
          containsIgnoringCase("[FN]+".toRegex())
        }

        test("fails when the subject does not match the regex") {
          assertThrows<AssertionFailedError> {
            containsIgnoringCase("\\d+".toRegex())
          }
        }
      }

      context("contains(CharSequence)") {
        test("passes when the subject contains the expected substring") {
          contains("nor")
        }

        test("fails when the subject contains the expected substring in a different case") {
          assertThrows<AssertionFailedError> {
            contains("NOR")
          }
        }

        test("fails when the subject does not contain the expected substring") {
          assertThrows<AssertionFailedError> {
            contains("meme")
          }
        }
      }

      context("containsIgnoringCase(CharSequence)") {
        test("passes when the subject contains the expected substring") {
          containsIgnoringCase("nor")
        }

        test("passes when the subject contains the expected substring in a different case") {
          containsIgnoringCase("NOR")
        }

        test("fails when the subject does not contain the expected substring") {
          assertThrows<AssertionFailedError> {
            containsIgnoringCase("meme")
          }
        }
      }

      derivedContext<Assertion.Builder<CharSequence?>>("isNullOrEmpty") {
        listOf("", null).forEach<CharSequence?> { subject ->
          context("when the subject is ${subject.quoted()}") {
            fixture { expectThat(subject) }

            test("the assertion passes") {
              isNullOrEmpty()
            }
          }
        }

        listOf("catflap", " ", "\t", "a", "23", "[]")
          .forEach<CharSequence?> { subject ->
            context("when the subject is ${subject.quoted()}") {
              fixture { expectThat(subject) }

              test("the assertion fails") {
                assertThrows<AssertionFailedError> {
                  isNullOrEmpty()
                }
              }
            }
          }
      }

      derivedContext<Assertion.Builder<CharSequence?>>("isNullOrBlank") {
        listOf("", null, "\t", "     ", " \n \r\n\t\n")
          .forEach<CharSequence?> { subject ->
            context("when the subject is ${subject.quoted()}") {
              fixture { expectThat(subject) }
              test("the assertion passes") {
                isNullOrBlank()
              }
            }
          }

        listOf("catflap", "a", "73", "[]").forEach<CharSequence?> { subject ->
          context("when the subject is ${subject.quoted()}") {
            fixture { expectThat(subject) }

            test("the assertion fails") {
              assertThrows<AssertionFailedError> {
                isNullOrBlank()
              }
            }
          }
        }
      }

      context("isEmpty") {
        context("when the subject is ${"".quoted()}") {
          fixture { expectThat("") }

          test("the assertion passes") {
            isEmpty()
          }
        }

        listOf("catflap", " ", "\t", "a", "73", "[]").forEach { subject ->
          context("${"when the subject is ${subject.quoted()}"} : ${subject.quoted()}") {
            fixture { expectThat(subject) }

            test("the assertion fails") {
              assertThrows<AssertionFailedError> {
                isEmpty()
              }
            }
          }
        }
      }

      context("isBlank") {
        listOf(
          "",
          "\t",
          "     ",
          " \n \r\n\t\n"
        ).forEach<CharSequence> { subject ->
          context("when the subject is ${subject.quoted()}") {
            fixture { expectThat(subject) }

            test("the assertion passes") {
              isBlank()
            }
          }
        }

        listOf("catflap", "a", "23", "[]").forEach<CharSequence> { subject ->
          context("when the subject is ${subject.quoted()}") {
            fixture { expectThat(subject) }

            test("the assertion fails") {
              assertThrows<AssertionFailedError> {
                isBlank()
              }
            }
          }
        }
      }

      context("isNotEmpty") {
        context("when the subject is ${"".quoted()}") {
          fixture { expectThat("") }

          test("the assertion fails") {
            assertThrows<AssertionFailedError> {
              isNotEmpty()
            }
          }
        }

        listOf("catflap", " ", "\t", "a", "73", "[]")
          .forEach<CharSequence> { subject ->
            context("when the subject is ${subject.quoted()}") {
              fixture { expectThat(subject) }

              test("the assertion passes") {
                isNotEmpty()
              }
            }
          }
      }

      context("isNotBlank") {
        listOf(
          "",
          "\t",
          "     ",
          " \n \r\n\t\n"
        ).forEach<CharSequence> { subject ->
          context("when the subject is ${subject.quoted()}") {
            fixture { expectThat(subject) }

            test("the assertion fails") {
              assertThrows<AssertionFailedError> {
                isNotBlank()
              }
            }
          }
        }

        listOf("catflap", "a", "73", "[]").forEach { subject ->
          context("when the subject is ${subject.quoted()}") {
            fixture { expectThat(subject) }
            test("the assertion passes") {
              isNotBlank()
            }
          }
        }
      }

      context("trim") {
        fixture { expectThat(StringBuilder(" fnord ")) }

        test("can trim char sequence") {
          trim().isEqualTo("fnord")
        }
      }
    }
}
