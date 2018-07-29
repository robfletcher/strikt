package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.fails

@DisplayName("assertions on CharSequence")
internal class CharSequenceAssertions {
  @Nested
  @DisplayName("hasLength assertion")
  inner class HasLength {
    @Test
    fun `passes if the subject has the expected length`() {
      expect("fnord").hasLength(5)
    }

    @Test
    fun `fails if the subject does not have the expected length`() {
      fails {
        expect("fnord").hasLength(1)
      }
    }
  }

  @Nested
  @DisplayName("matches assertion")
  inner class Matches {
    @Test
    fun `passes if the subject is a full match for the regex`() {
      expect("fnord").matches("[dfnor]+".toRegex())
    }

    @Test
    fun `fails if the subject is only a partial match for the regex`() {
      fails {
        expect("despite the negative press fnord").matches("[dfnor]+".toRegex())
      }
    }

    @Test
    fun `fails if the subject is a case insensitive match for the regex`() {
      fails {
        expect("fnord").matches("[DFNOR]+".toRegex())
      }
    }

    @Test
    fun `fails if the subject does not match the regex`() {
      fails {
        expect("fnord").matches("\\d+".toRegex())
      }
    }
  }

  @Nested
  @DisplayName("matchesIgnoringCase assertion")
  inner class MatchesIgnoringCase {
    @Test
    fun `passes if the subject is a full match for the regex`() {
      expect("fnord").matchesIgnoringCase("[dfnor]+".toRegex())
    }

    @Test
    fun `fails if the subject is only a partial match for the regex`() {
      fails {
        expect("despite the negative press fnord").matchesIgnoringCase("[dfnor]+".toRegex())
      }
    }

    @Test
    fun `passes if the subject is a case insensitive match for the regex`() {
      expect("fnord").matchesIgnoringCase("[DFNOR]+".toRegex())
    }

    @Test
    fun `fails if the subject does not match the regex`() {
      fails {
        expect("fnord").matchesIgnoringCase("\\d+".toRegex())
      }
    }
  }

  @Nested
  @DisplayName("contains(Regex) assertion")
  inner class Contains_Regex {
    @Test
    fun `passes if the subject is a full match for the regex`() {
      expect("fnord").contains("[dfnor]+".toRegex())
    }

    @Test
    fun `passes if the subject is only a partial match for the regex`() {
      expect("despite the negative press fnord").contains("[dfnor]+".toRegex())
    }

    @Test
    fun `passes if the subject contains a match with a different case`() {
      fails {
        expect("despite the negative press fnord").contains("[DFNOR]+".toRegex())
      }
    }

    @Test
    fun `fails if the subject does not match the regex`() {
      fails {
        expect("fnord").contains("\\d+".toRegex())
      }
    }
  }

  @Nested
  @DisplayName("containsIgnoringCase(Regex) assertion")
  inner class ContainsIgnoringCase_Regex {
    @Test
    fun `passes if the subject is a full match for the regex`() {
      expect("fnord").containsIgnoringCase("[dfnor]+".toRegex())
    }

    @Test
    fun `passes if the subject is only a partial match for the regex`() {
      expect("despite the negative press fnord").containsIgnoringCase("[dfnor]+".toRegex())
    }

    @Test
    fun `passes if the subject contains a match with a different case`() {
      expect("despite the negative press fnord").containsIgnoringCase("[DFNOR]+".toRegex())
    }

    @Test
    fun `fails if the subject does not match the regex`() {
      fails {
        expect("fnord").containsIgnoringCase("\\d+".toRegex())
      }
    }
  }

  @Nested
  @DisplayName("contains(CharSequence) assertion")
  inner class Contains_CharSequence {
    @Test
    fun `passes if the subject contains the expected substring`() {
      expect("fnord").contains("nor")
    }

    @Test
    fun `fails if the subject contains the expected substring in a different case`() {
      fails {
        expect("fnord").contains("NOR")
      }
    }

    @Test
    fun `fails if the subject does not contain the expected substring`() {
      fails {
        expect("fnord").contains("meme")
      }
    }
  }

  @Nested
  @DisplayName("containsIgnoringCase(CharSequence) assertion")
  inner class ContainsIgnoringCase_CharSequence {
    @Test
    fun `passes if the subject contains the expected substring`() {
      expect("fnord").containsIgnoringCase("nor")
    }

    @Test
    fun `passes if the subject contains the expected substring in a different case`() {
      expect("fnord").containsIgnoringCase("NOR")
    }

    @Test
    fun `fails if the subject does not contain the expected substring`() {
      fails {
        expect("fnord").containsIgnoringCase("meme")
      }
    }
  }
}
