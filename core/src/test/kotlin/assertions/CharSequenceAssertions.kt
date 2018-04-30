package assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

internal object CharSequenceAssertions : Spek({

  describe("hasLength assertion") {
    it("passes if the target has the expected length") {
      passes {
        expect("covfefe").hasLength(7)
      }
    }
    it("fails if the target does not have the expected length") {
      fails {
        expect("covfefe").hasLength(1)
      }
    }
  }
})
