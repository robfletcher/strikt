package strikt.assertions

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import strikt.api.expect
import strikt.fails

internal object CharSequenceAssertions : Spek({
  describe("assertions on ${CharSequence::class.simpleName}") {
    describe("hasLength assertion") {
      it("passes if the subject has the expected length") {
        expect("fnord").hasLength(5)
      }
      it("fails if the subject does not have the expected length") {
        fails {
          expect("fnord").hasLength(1)
        }
      }
    }

    describe("matches assertion") {
      it("passes if the subject is a full match for the regex") {
        expect("fnord").matches("[dfnor]+".toRegex())
      }
      it("fails if the subject is only a partial match for the regex") {
        fails {
          expect("despite the negative press fnord").matches("[dfnor]+".toRegex())
        }
      }
      it("fails if the subject is a case insensitive match for the regex") {
        fails {
          expect("fnord").matches("[DFNOR]+".toRegex())
        }
      }
      it("fails if the subject does not match the regex") {
        fails {
          expect("fnord").matches("\\d+".toRegex())
        }
      }
    }

    describe("matchesIgnoringCase assertion") {
      it("passes if the subject is a full match for the regex") {
        expect("fnord").matchesIgnoringCase("[dfnor]+".toRegex())
      }
      it("fails if the subject is only a partial match for the regex") {
        fails {
          expect("despite the negative press fnord").matchesIgnoringCase("[dfnor]+".toRegex())
        }
      }
      it("passes if the subject is a case insensitive match for the regex") {
        expect("fnord").matchesIgnoringCase("[DFNOR]+".toRegex())
      }
      it("fails if the subject does not match the regex") {
        fails {
          expect("fnord").matchesIgnoringCase("\\d+".toRegex())
        }
      }
    }

    describe("contains(Regex) assertion") {
      it("passes if the subject is a full match for the regex") {
        expect("fnord").contains("[dfnor]+".toRegex())
      }
      it("passes if the subject is only a partial match for the regex") {
        expect("despite the negative press fnord").contains("[dfnor]+".toRegex())
      }
      it("passes if the subject contains a match with a different case") {
        fails {
          expect("despite the negative press fnord").contains("[DFNOR]+".toRegex())
        }
      }
      it("fails if the subject does not match the regex") {
        fails {
          expect("fnord").contains("\\d+".toRegex())
        }
      }
    }

    describe("containsIgnoringCase(Regex) assertion") {
      it("passes if the subject is a full match for the regex") {
        expect("fnord").containsIgnoringCase("[dfnor]+".toRegex())
      }
      it("passes if the subject is only a partial match for the regex") {
        expect("despite the negative press fnord").containsIgnoringCase("[dfnor]+".toRegex())
      }
      it("passes if the subject contains a match with a different case") {
        expect("despite the negative press fnord").containsIgnoringCase("[DFNOR]+".toRegex())
      }
      it("fails if the subject does not match the regex") {
        fails {
          expect("fnord").containsIgnoringCase("\\d+".toRegex())
        }
      }
    }

    describe("contains(CharSequence) assertion") {
      it("passes if the subject contains the expected substring") {
        expect("fnord").contains("nor")
      }
      it("fails if the subject contains the expected substring in a different case") {
        fails {
          expect("fnord").contains("NOR")
        }
      }
      it("fails if the subject does not contain the expected substring") {
        fails {
          expect("fnord").contains("meme")
        }
      }
    }

    describe("containsIgnoringCase(CharSequence) assertion") {
      it("passes if the subject contains the expected substring") {
        expect("fnord").containsIgnoringCase("nor")
      }
      it("passes if the subject contains the expected substring in a different case") {
        expect("fnord").containsIgnoringCase("NOR")
      }
      it("fails if the subject does not contain the expected substring") {
        fails {
          expect("fnord").containsIgnoringCase("meme")
        }
      }
    }
  }
})
