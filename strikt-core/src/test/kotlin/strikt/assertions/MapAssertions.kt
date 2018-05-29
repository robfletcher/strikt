package strikt.assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import strikt.api.expect
import strikt.fails

internal object MapAssertions : Spek({
  describe("Assertions on ${Map::class.simpleName}") {
    describe("isEmpty assertion") {
      it("passes if the subject is empty") {
        val subject = emptyMap<Any, Any>()
        expect(subject).isEmpty()
      }
      it("fails if the subject is not empty") {
        fails {
          val subject = mapOf("Eris" to "Strife and confusion")
          expect(subject).isEmpty()
        }
      }
    }
  }
})