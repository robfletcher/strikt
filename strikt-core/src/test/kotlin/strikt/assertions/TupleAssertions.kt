package strikt.assertions

import org.junit.jupiter.api.Test
import strikt.api.expect

internal class TupleAssertions {
  @Test fun `first maps assertion to component1 of a pair`() {
    expect("a" to 1).first.isEqualTo("a")
  }

  @Test fun `second maps assertion to component2 of a pair`() {
    expect("a" to 1).second.isEqualTo(1)
  }

  @Test fun `first maps assertion to component1 of a triple`() {
    expect(Triple("a", "b", 1)).first.isEqualTo("a")
  }

  @Test fun `second maps assertion to component2 of a triple`() {
    expect(Triple("a", "b", 1)).second.isEqualTo("b")
  }

  @Test fun `third maps assertion to component3 of a triple`() {
    expect(Triple("a", "b", 1)).third.isEqualTo(1)
  }
}
