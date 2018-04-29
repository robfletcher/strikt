package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object SpecializedAssertions : Spek({

  describe("assertion chain on a string target") {
    it("allows more specialized assertions") {
      shouldPass {
        expect("covfefe").hasLength(7)
      }
    }
  }

  describe("downcasting an unspecified type to one with a more specialized assertion") {
    it("allows more specialized assertions") {
      shouldPass {
        val target: Any = "covfefe"
        expect(target).isA<String>().hasLength(7)
      }
    }
  }
})