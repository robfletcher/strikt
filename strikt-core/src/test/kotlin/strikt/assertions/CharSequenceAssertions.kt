package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat

@DisplayName("assertions on CharSequence")
internal object CharSequenceAssertions {
  @TestFactory
  fun hasLength() = assertionTests<CharSequence> {
    fixture { expectThat("fnord") }

    test("passes when the subject has the expected length") {
      hasLength(5)
    }

    test("fails when the subject does not have the expected length") {
      assertThrows<AssertionFailedError> {
        hasLength(1)
      }
    }
  }

  @TestFactory
  fun matches() = assertionTests<CharSequence> {
    fixture { expectThat("fnord") }

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

  @TestFactory
  fun matchesIgnoringCase() = assertionTests<CharSequence> {
    fixture { expectThat("fnord") }

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

  @TestFactory
  @DisplayName("contains(Regex)")
  fun contains_Regex() = assertionTests<CharSequence> {
    fixture { expectThat("fnord") }

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

  @TestFactory
  @DisplayName("containsIgnoringCase(Regex)")
  fun containsIgnoringCase_Regex() = assertionTests<CharSequence> {
    fixture { expectThat("fnord") }

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

  @TestFactory
  @DisplayName("contains(CharSequence)")
  fun contains_CharSequence() = assertionTests<CharSequence> {
    fixture { expectThat("fnord") }

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

  @TestFactory
  @DisplayName("containsIgnoringCase(CharSequence)")
  fun containsIgnoringCase_CharSequence() = assertionTests<CharSequence> {
    fixture { expectThat("fnord") }

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

  @TestFactory
  fun isNullOrEmpty() = assertionTests<CharSequence?> {
    listOf("", null).forEach<CharSequence?> {
      context("when the subject is ${it.quoted()}") {
        fixture { expectThat(it) }

        test("the assertion passes") {
          isNullOrEmpty()
        }
      }
    }

    listOf("catflap", " ", "\t", "a", "23", "[]").forEach<CharSequence?> {
      context("when the subject is ${it.quoted()}") {
        fixture { expectThat(it) }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isNullOrEmpty()
          }
        }
      }
    }
  }

  @TestFactory
  fun isNullOrBlank() = assertionTests<CharSequence?> {
    listOf("", null, "\t", "     ", " \n \r\n\t\n").forEach<CharSequence?> {
      context("when the subject is ${it.quoted()}") {
        fixture { expectThat(it) }
        test("the assertion passes") {
          isNullOrBlank()
        }
      }
    }

    listOf("catflap", "a", "73", "[]").forEach<CharSequence?> {
      context("when the subject is ${it.quoted()}") {
        fixture { expectThat(it) }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isNullOrBlank()
          }
        }
      }
    }
  }

  @TestFactory
  fun isEmpty() = assertionTests<CharSequence> {
    context("when the subject is ${"".quoted()}") {
      fixture { expectThat("") }

      test("the assertion passes") {
        isEmpty()
      }
    }

    listOf("catflap", " ", "\t", "a", "73", "[]").forEach {
      context("${"when the subject is ${it.quoted()}"} : ${it.quoted()}") {
        fixture { expectThat(it) }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isEmpty()
          }
        }
      }
    }
  }

  @TestFactory
  fun isBlank() = assertionTests<CharSequence> {
    listOf("", "\t", "     ", " \n \r\n\t\n").forEach<CharSequence> {
      context("when the subject is ${it.quoted()}") {
        fixture { expectThat(it) }

        test("the assertion passes") {
          isBlank()
        }
      }
    }

    listOf("catflap", "a", "23", "[]").forEach<CharSequence> {
      context("when the subject is ${it.quoted()}") {
        fixture { expectThat(it) }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isBlank()
          }
        }
      }
    }
  }

  @TestFactory
  fun isNotEmpty() = assertionTests<CharSequence> {
    context("when the subject is ${"".quoted()}") {
      fixture { expectThat("") }

      test("the assertion fails") {
        assertThrows<AssertionFailedError> {
          isNotEmpty()
        }
      }
    }

    listOf("catflap", " ", "\t", "a", "73", "[]").forEach<CharSequence> {
      context("when the subject is ${it.quoted()}") {
        fixture { expectThat(it) }

        test("the assertion passes") {
          isNotEmpty()
        }
      }
    }
  }

  @TestFactory
  fun isNotBlank() = assertionTests<CharSequence> {
    listOf("", "\t", "     ", " \n \r\n\t\n").forEach<CharSequence> {
      context("when the subject is ${it.quoted()}") {
        fixture { expectThat(it) }

        test("the assertion fails") {
          assertThrows<AssertionFailedError> {
            isNotBlank()
          }
        }
      }
    }

    listOf("catflap", "a", "73", "[]").forEach {
      context("when the subject is ${it.quoted()}") {
        fixture { expectThat(it) }
        test("the assertion passes") {
          isNotBlank()
        }
      }
    }
  }

  @Test
  fun `can trim char sequence`() {
    expectThat(StringBuilder(" fnord ")).trim().isEqualTo("fnord")
  }
}
