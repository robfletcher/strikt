package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import strikt.api.expect
import strikt.fails

internal object StringAssertions : Spek({
  describe("assertions on ${String::class.simpleName}") {
    describe("isEqualToIgnoringCase assertion") {
      it("passes if the subject is identical to the expected value") {
        expect("fnord").isEqualToIgnoringCase("fnord")
      }
      it("fails if the subject is different") {
        fails {
          expect("despite the negative press fnord").isEqualToIgnoringCase("fnord")
        }
      }
      it("passes if the subject is the same as the expected value apart from case") {
        expect("fnord").isEqualToIgnoringCase("fnord")
      }
    }

    describe("block assertions on string subjects") {
      it("compiles") {
        fails {
          val subject = "The Enlightened take things Lightly"
          expect(subject = subject) {
            hasLength(5)
            matches(Regex("\\d+"))
            startsWith("T")
          }
        }.let { e ->
          assertEquals(2, e.failures.size)
        }
      }
    }
  }
})
