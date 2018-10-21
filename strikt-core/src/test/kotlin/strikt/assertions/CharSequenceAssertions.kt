package strikt.assertions

import com.oneeyedmen.minutest.junit.junitTests
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.fails

@DisplayName("assertions on CharSequence")
internal object CharSequenceAssertions {
  @TestFactory
  fun hasLength() = junitTests<Pair<CharSequence, Int>> {
    context("the subject has the expected length") {
      fixture { "fnord" to 5 }

      test("the assertion passes") {
        expectThat(first).hasLength(second)
      }
    }

    context("the subject does not have the expected length") {
      fixture { "fnord" to 1 }

      test("the assertion fails") {
        fails {
          expectThat(first).hasLength(second)
        }
      }
    }
  }

  @TestFactory
  fun matches() = junitTests<Pair<CharSequence, Regex>> {
    context("the subject is a full match for the regex") {
      fixture { "fnord" to "[dfnor]+".toRegex() }

      test("the assertion passes") {
        expectThat(first).matches(second)
      }
    }

    context("the subject is only a partial match for the regex") {
      fixture { "despite the negative press fnord" to "[dfnor]+".toRegex() }

      test("the assertion fails") {
        fails {
          expectThat(first).matches(second)
        }
      }
    }

    context("the subject is a case insensitive match for the regex") {
      fixture { "fnord" to "[DFNOR]+".toRegex() }

      test("the assertion fails") {
        fails {
          expectThat(first).matches(second)
        }
      }
    }

    context("the subject does not match the regex") {
      fixture { "fnord" to "\\d+".toRegex() }

      test("the assertion fails") {
        fails {
          expectThat(first).matches(second)
        }
      }
    }
  }

  @TestFactory
  fun matchesIgnoringCase() = junitTests<Pair<CharSequence, Regex>> {
    context("the subject is a full match for the regex") {
      fixture { "fnord" to "[dfnor]+".toRegex() }

      test("the assertion passes") {
        expectThat(first).matchesIgnoringCase(second)
      }
    }

    context("the subject is only a partial match for the regex") {
      fixture { "despite the negative press fnord" to "[dfnor]+".toRegex() }

      test("the assertion fails") {
        fails {
          expectThat(first).matchesIgnoringCase(second)
        }
      }
    }

    context("the subject is a case insensitive match for the regex") {
      fixture { "fnord" to "[DFNOR]+".toRegex() }

      test("the assertion passes") {
        expectThat(first).matchesIgnoringCase(second)
      }
    }

    context("the subject does not match the regex") {
      fixture { "fnord" to "\\d+".toRegex() }

      test("the assertion fails") {
        fails {
          expectThat(first).matchesIgnoringCase(second)
        }
      }
    }
  }

  @TestFactory
  @DisplayName("contains(Regex) assertion")
  fun contains_Regex() = junitTests<Pair<CharSequence, Regex>> {
    context("the subject is a full match for the regex") {
      fixture { "fnord" to "[dfnor]+".toRegex() }

      test("the assertion passes") {
        expectThat(first).contains(second)
      }
    }

    context("the subject is only a partial match for the regex") {
      fixture { "despite the negative press fnord" to "[dfnor]+".toRegex() }

      test("the assertion passes") {
        expectThat(first).contains(second)
      }
    }

    context("the subject contains a match with a different case") {
      fixture { "despite the negative press fnord" to "[DFNOR]+".toRegex() }

      test("the assertion fails") {
        fails {
          expectThat(first).contains(second)
        }
      }
    }

    context("the subject does not match the regex") {
      fixture { "fnord" to "\\d+".toRegex() }

      test("the assertion fails") {
        fails {
          expectThat(first).contains(second)
        }
      }
    }
  }

  @TestFactory
  @DisplayName("containsIgnoringCase(Regex) assertion")
  fun containsIgnoringCase_Regex() = junitTests<Pair<CharSequence, Regex>> {
    context("the subject is a full match for the regex") {
      fixture { "fnord" to "[dfnor]+".toRegex() }

      test("the assertion passes") {
        expectThat(first).containsIgnoringCase(second)
      }
    }

    context("the subject is only a partial match for the regex") {
      fixture { "despite the negative press fnord" to "[dfnor]+".toRegex() }

      test("the assertion passes") {
        expectThat(first).containsIgnoringCase(second)
      }
    }

    context("the subject contains a match with a different case") {
      fixture { "despite the negative press fnord" to "[DFNOR]+".toRegex() }

      test("the assertion passes") {
        expectThat(first).containsIgnoringCase(second)
      }
    }

    context("the subject does not match the regex") {
      fixture { "fnord" to "\\d+".toRegex() }

      test("the assertion fails") {
        fails {
          expectThat(first).containsIgnoringCase(second)
        }
      }
    }
  }

  @TestFactory
  @DisplayName("contains(CharSequence) assertion")
  fun contains_CharSequence() = junitTests<Pair<CharSequence, CharSequence>> {
    context("the subject contains the expected substring") {
      fixture { "fnord" to "nor" }

      test("the assertion passes") {
        expectThat(first).contains(second)
      }
    }

    context("the subject contains the expected substring in a different case") {
      fixture { "fnord" to "NOR" }

      test("the assertion fails") {
        fails {
          expectThat(first).contains(second)
        }
      }
    }

    context("the subject does not contain the expected substring") {
      fixture { "fnord" to "meme" }

      test("the assertion fails") {
        fails {
          expectThat(first).contains(second)
        }
      }
    }
  }

  @TestFactory
  @DisplayName("containsIgnoringCase(CharSequence) assertion")
  fun containsIgnoringCase_CharSequence() = junitTests<Pair<CharSequence, CharSequence>> {
    context("the subject contains the expected substring") {
      fixture { "fnord" to "nor" }

      test("the assertion passes") {
        expectThat(first).containsIgnoringCase(second)
      }
    }

    context("the subject contains the expected substring in a different case") {
      fixture { "fnord" to "NOR" }

      test("the assertion passes") {
        expectThat(first).containsIgnoringCase(second)
      }
    }

    context("the subject does not contain the expected substring") {
      fixture { "fnord" to "meme" }

      test("the assertion fails") {
        fails {
          expectThat(first).containsIgnoringCase(second)
        }
      }
    }
  }

  @TestFactory
  @DisplayName("isNullOrEmpty assertion")
  fun isNullOrEmpty() = junitTests<CharSequence?> {
    listOf("", null).map { subject ->
      context("the subject is ${if (subject == null) "null" else "\"$subject\""}") {
        fixture { subject }

        test("the assertion passes") {
          expectThat(this).isNullOrEmpty()
        }
      }
    }

    listOf("catflap", " ", "\t", "a", "73", "[]").map { subject ->
      context("the subject is \"$subject\"") {
        fixture { subject }

        test("the assertion fails") {
          fails {
            expectThat(this).isNullOrEmpty()
          }
        }
      }
    }
  }

  @TestFactory
  fun isNullOrBlank() = junitTests<CharSequence?> {
    listOf("", null, "\t", "     ", " \n \r\n\t\n").map { subject ->
      context("the subject is ${if (subject == null) "null" else "\"$subject\""}") {
        fixture { subject }

        test("the assertion passes") {
          expectThat(this).isNullOrBlank()
        }
      }
    }

    listOf("catflap", "a", "73", "[]").map { subject ->
      context("the subject is \"$subject\"") {
        fixture { subject }

        test("the assertion fails") {
          fails {
            expectThat(this).isNullOrBlank()
          }
        }
      }
    }
  }

  @TestFactory
  fun isEmpty() = junitTests<CharSequence> {
    listOf("").map { subject ->
      context("subject is \"$subject\"") {
        fixture { subject }

        test("the assertion passes") {
          expectThat(this).isEmpty()
        }
      }
    }

    listOf("catflap", " ", "\t", "a", "73", "[]").map { subject ->
      context("the subject is \"$subject\"") {
        fixture { subject }

        test("the assertion fails") {
          fails {
            expectThat(this).isEmpty()
          }
        }
      }
    }
  }

  @TestFactory
  fun isBlank() = junitTests<CharSequence> {
    listOf("", "\t", "     ", " \n \r\n\t\n").map { subject ->
      context("the subject is \"$subject\"") {
        fixture { subject }

        test("the assertion passes") {
          expectThat(this).isBlank()
        }
      }
    }

    listOf("catflap", "a", "73", "[]").map {
      context("the subject is \"$it\"") {
        fixture { it }

        test("the assertion fails") {
          fails {
            expectThat(this).isBlank()
          }
        }
      }
    }
  }

  @TestFactory
  fun isNotEmpty() = junitTests<CharSequence> {
    listOf("").map { subject ->
      context("the subject is \"$subject\"") {
        fixture { subject }

        test("the assertion fails") {
          fails {
            expectThat(this).isNotEmpty()
          }
        }
      }
    }

    listOf("catflap", " ", "\t", "a", "73", "[]").map { subject ->
      context("the subject is \"$subject\"") {
        fixture { subject }

        test("the assertion passes") {
          expectThat(this).isNotEmpty()
        }
      }
    }
  }

  @TestFactory
  @DisplayName("isNotBlank assertion")
  fun isNotBlank() = junitTests<CharSequence> {
    listOf("", "\t", "     ", " \n \r\n\t\n").map { subject ->
      context("the subject is \"$subject\"") {
        fixture { subject }

        test("the assertion fails") {
          fails {
            expectThat(this).isNotBlank()
          }
        }
      }
    }

    listOf("catflap", "a", "73", "[]").map { subject ->
      context("the subject is \"$subject\"") {
        fixture { subject }

        test("the assertion passes") {
          expectThat(subject).isNotBlank()
        }
      }
    }
  }

  @Test
  fun `can trim char sequence`() {
    expectThat(StringBuilder(" fnord ")).trim().isEqualTo("fnord")
  }
}
