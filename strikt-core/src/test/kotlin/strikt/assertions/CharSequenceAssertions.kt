package strikt.assertions

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import strikt.api.expect
import strikt.fails

internal object CharSequenceAssertions : Spek({
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
      it("fails if the subject is a case insensitive match for the regex") {
        fails {
          expect("covfefe").matches("[CEFOV]+".toRegex())
        }
      }
      it("fails if the subject does not match the regex") {
        fails {
          expect("covfefe").matches("\\d+".toRegex())
        }
      }
    }

    describe("matchesIgnoringCase assertion") {
      it("passes if the subject is a full match for the regex") {
        expect("covfefe").matchesIgnoringCase("[cefov]+".toRegex())
      }
      it("fails if the subject is only a partial match for the regex") {
        fails {
          expect("despite the negative press covfefe").matchesIgnoringCase("[cefov]+".toRegex())
        }
      }
      it("passes if the subject is a case insensitive match for the regex") {
        expect("covfefe").matchesIgnoringCase("[CEFOV]+".toRegex())
      }
      it("fails if the subject does not match the regex") {
        fails {
          expect("covfefe").matchesIgnoringCase("\\d+".toRegex())
        }
      }
    }

    describe("contains(Regex) assertion") {
      it("passes if the subject is a full match for the regex") {
        expect("covfefe").contains("[cefov]+".toRegex())
      }
      it("passes if the subject is only a partial match for the regex") {
        expect("despite the negative press covfefe").contains("[cefov]+".toRegex())
      }
      it("passes if the subject contains a match with a different case") {
        fails {
          expect("despite the negative press covfefe").contains("[CEFOV]+".toRegex())
        }
      }
      it("fails if the subject does not match the regex") {
        fails {
          expect("covfefe").contains("\\d+".toRegex())
        }
      }
    }

    describe("containsIgnoringCase(Regex) assertion") {
      it("passes if the subject is a full match for the regex") {
        expect("covfefe").containsIgnoringCase("[cefov]+".toRegex())
      }
      it("passes if the subject is only a partial match for the regex") {
        expect("despite the negative press covfefe").containsIgnoringCase("[cefov]+".toRegex())
      }
      it("passes if the subject contains a match with a different case") {
        expect("despite the negative press covfefe").containsIgnoringCase("[CEFOV]+".toRegex())
      }
      it("fails if the subject does not match the regex") {
        fails {
          expect("covfefe").containsIgnoringCase("\\d+".toRegex())
        }
      }
    }

    describe("contains(CharSequence) assertion") {
      it("passes if the subject contains the expected substring") {
        expect("covfefe").contains("ovf")
      }
      it("fails if the subject contains the expected substring in a different case") {
        fails {
          expect("covfefe").contains("OVF")
        }
      }
      it("fails if the subject does not contain the expected substring") {
        fails {
          expect("covfefe").contains("presidential")
        }
      }
    }

    describe("containsIgnoringCase(CharSequence) assertion") {
      it("passes if the subject contains the expected substring") {
        expect("covfefe").containsIgnoringCase("ovf")
      }
      it("passes if the subject contains the expected substring in a different case") {
        expect("covfefe").containsIgnoringCase("OVF")
      }
      it("fails if the subject does not contain the expected substring") {
        fails {
          expect("covfefe").containsIgnoringCase("presidential")
        }
      }
    }
  }
})
