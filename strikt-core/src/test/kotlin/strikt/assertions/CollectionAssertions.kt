package strikt.assertions

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import strikt.api.expect
import strikt.fails

internal object CollectionAssertions : Spek({
  describe("assertions on ${Collection::class.simpleName}") {
    describe("hasSize assertion") {
      it("fails if the subject size is not the expected size") {
        fails {
          val subject = setOf("catflap", "rubberplant", "marzipan")
          expect(subject).hasSize(1)
        }
      }

      describe("isEmpty assertion") {
        it("passes if collection is empty") {
          expect(emptyList<AnyAssertions>()).isEmpty()
        }
        it("fails if the collection is not empty") {
          fails {
            expect(listOf("catflap", "rubberplant", "marzipan")).isEmpty()
          }
        }
      }

      describe("isNotEmpty assertion") {
        it("fails if collection is empty") {
          fails {
            expect(emptyList<AnyAssertions>()).isNotEmpty()
          }
        }
        it("passes if the collection is not empty") {
          expect(listOf("catflap", "rubberplant", "marzipan")).isNotEmpty()
        }
      }
    }
  }
})
