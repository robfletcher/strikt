package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.fails

@DisplayName("assertions on CharSequence")
internal class CharSequenceAssertions {
  @Nested
  @DisplayName("hasLength assertion")
  inner class HasLength {
    @Test
    fun `passes if the subject has the expected length`() {
      expectThat("fnord").hasLength(5)
    }

    @Test
    fun `fails if the subject does not have the expected length`() {
      fails {
        expectThat("fnord").hasLength(1)
      }
    }
  }

  @Nested
  @DisplayName("matches assertion")
  inner class Matches {
    @Test
    fun `passes if the subject is a full match for the regex`() {
      expectThat("fnord").matches("[dfnor]+".toRegex())
    }

    @Test
    fun `fails if the subject is only a partial match for the regex`() {
      fails {
        expectThat("despite the negative press fnord")
          .matches("[dfnor]+".toRegex())
      }
    }

    @Test
    fun `fails if the subject is a case insensitive match for the regex`() {
      fails {
        expectThat("fnord").matches("[DFNOR]+".toRegex())
      }
    }

    @Test
    fun `fails if the subject does not match the regex`() {
      fails {
        expectThat("fnord").matches("\\d+".toRegex())
      }
    }
  }

  @Nested
  @DisplayName("matchesIgnoringCase assertion")
  inner class MatchesIgnoringCase {
    @Test
    fun `passes if the subject is a full match for the regex`() {
      expectThat("fnord").matchesIgnoringCase("[dfnor]+".toRegex())
    }

    @Test
    fun `fails if the subject is only a partial match for the regex`() {
      fails {
        expectThat("despite the negative press fnord")
          .matchesIgnoringCase("[dfnor]+".toRegex())
      }
    }

    @Test
    fun `passes if the subject is a case insensitive match for the regex`() {
      expectThat("fnord").matchesIgnoringCase("[DFNOR]+".toRegex())
    }

    @Test
    fun `fails if the subject does not match the regex`() {
      fails {
        expectThat("fnord").matchesIgnoringCase("\\d+".toRegex())
      }
    }
  }

  @Nested
  @DisplayName("contains(Regex) assertion")
  inner class Contains_Regex {
    @Test
    fun `passes if the subject is a full match for the regex`() {
      expectThat("fnord").contains("[dfnor]+".toRegex())
    }

    @Test
    fun `passes if the subject is only a partial match for the regex`() {
      expectThat("despite the negative press fnord")
        .contains("[dfnor]+".toRegex())
    }

    @Test
    fun `passes if the subject contains a match with a different case`() {
      fails {
        expectThat("despite the negative press fnord")
          .contains("[DFNOR]+".toRegex())
      }
    }

    @Test
    fun `fails if the subject does not match the regex`() {
      fails {
        expectThat("fnord").contains("\\d+".toRegex())
      }
    }
  }

  @Nested
  @DisplayName("containsIgnoringCase(Regex) assertion")
  inner class ContainsIgnoringCase_Regex {
    @Test
    fun `passes if the subject is a full match for the regex`() {
      expectThat("fnord").containsIgnoringCase("[dfnor]+".toRegex())
    }

    @Test
    fun `passes if the subject is only a partial match for the regex`() {
      expectThat("despite the negative press fnord")
        .containsIgnoringCase("[dfnor]+".toRegex())
    }

    @Test
    fun `passes if the subject contains a match with a different case`() {
      expectThat("despite the negative press fnord")
        .containsIgnoringCase("[DFNOR]+".toRegex())
    }

    @Test
    fun `fails if the subject does not match the regex`() {
      fails {
        expectThat("fnord").containsIgnoringCase("\\d+".toRegex())
      }
    }
  }

  @Nested
  @DisplayName("contains(CharSequence) assertion")
  inner class Contains_CharSequence {
    @Test
    fun `passes if the subject contains the expected substring`() {
      expectThat("fnord").contains("nor")
    }

    @Test
    fun `fails if the subject contains the expected substring in a different case`() {
      fails {
        expectThat("fnord").contains("NOR")
      }
    }

    @Test
    fun `fails if the subject does not contain the expected substring`() {
      fails {
        expectThat("fnord").contains("meme")
      }
    }
  }

  @Nested
  @DisplayName("containsIgnoringCase(CharSequence) assertion")
  inner class ContainsIgnoringCase_CharSequence {
    @Test
    fun `passes if the subject contains the expected substring`() {
      expectThat("fnord").containsIgnoringCase("nor")
    }

    @Test
    fun `passes if the subject contains the expected substring in a different case`() {
      expectThat("fnord").containsIgnoringCase("NOR")
    }

    @Test
    fun `fails if the subject does not contain the expected substring`() {
      fails {
        expectThat("fnord").containsIgnoringCase("meme")
      }
    }
  }

  @Nested
  @DisplayName("isNullOrEmpty assertion")
  inner class IsNullOrEmpty {
    @TestFactory
    fun passesIfStringIsNullOrEmpty() =
      listOf("", null).map { subject ->
        dynamicTest("passes if subject is ${if (subject == null) "null" else "\"$subject\""}") {
          expectThat(subject).isNullOrEmpty()
        }
      }

    @TestFactory
    fun failsIfStringIsNotEmpty() =
      listOf("catflap", " ", "\t", "a", "73", "[]").map { subject ->
        dynamicTest("fails if subject is \"$subject\"") {
          fails {
            expectThat(subject).isNullOrEmpty()
          }
        }
      }
  }

  @Nested
  @DisplayName("isNullOrBlank assertion")
  inner class IsNullOrBlank {
    @TestFactory
    fun passesIfStringIsNullOrBlank() =
      listOf("", null, "\t", "     ", " \n \r\n\t\n").map { subject ->
        dynamicTest("passes if subject is ${if (subject == null) "null" else "\"$subject\""}") {
          expectThat(subject).isNullOrBlank()
        }
      }

    @TestFactory
    fun failsIfStringIsNotBlank() =
      listOf("catflap", "a", "73", "[]").map { subject ->
        dynamicTest("fails if subject is \"$subject\"") {
          fails {
            expectThat(subject).isNullOrBlank()
          }
        }
      }
  }

  @Nested
  @DisplayName("isEmpty assertion")
  inner class IsEmpty {
    @TestFactory
    fun passesIfStringIsEmpty() =
      listOf("").map { subject ->
        dynamicTest("passes if subject is \"$subject\"") {
          expectThat("").isEmpty()
        }
      }

    @TestFactory
    fun failsIfStringIsNotEmpty() =
      listOf("catflap", " ", "\t", "a", "73", "[]").map { subject ->
        dynamicTest("fails if subject is \"$subject\"") {
          fails {
            expectThat(subject).isEmpty()
          }
        }
      }
  }

  @Nested
  @DisplayName("isBlank assertion")
  inner class IsBlank {
    @TestFactory
    fun passesIfStringIsBlank() =
      listOf("", "\t", "     ", " \n \r\n\t\n").map {
        dynamicTest("passes if subject is \"$it\"") {
          expectThat(it).isBlank()
        }
      }

    @TestFactory
    fun failsIfStringIsNotBlank() =
      listOf("catflap", "a", "73", "[]").map {
        dynamicTest("fails if subject is \"$it\"") {
          fails {
            expectThat(it).isBlank()
          }
        }
      }
  }

  @Nested
  @DisplayName("isNotEmpty assertion")
  inner class IsNotEmpty {
    @TestFactory
    fun failsIfStringIsEmpty() =
      listOf("").map {
        dynamicTest("passes if subject is \"$it\"") {
          fails {
            expectThat("").isNotEmpty()
          }
        }
      }

    @TestFactory
    fun passesIfStringIsNotEmpty() =
      listOf("catflap", " ", "\t", "a", "73", "[]").map {
        dynamicTest("fails if subject is \"$it\"") {
          expectThat(it).isNotEmpty()
        }
      }
  }

  @Nested
  @DisplayName("isNotBlank assertion")
  inner class IsNotBlank {
    @TestFactory
    fun failsIfStringIsBlank() =
      listOf("", "\t", "     ", " \n \r\n\t\n").map {
        dynamicTest("passes if subject is \"$it\"") {
          fails {
            expectThat(it).isNotBlank()
          }
        }
      }

    @TestFactory
    fun passesIfStringIsNotBlank() =
      listOf("catflap", "a", "73", "[]").map {
        dynamicTest("fails if subject is \"$it\"") {
          expectThat(it).isNotBlank()
        }
      }
  }

  @Test
  fun `can trim char sequence`() {
    expectThat(StringBuilder(" fnord ")).trim().isEqualTo("fnord")
  }

}
