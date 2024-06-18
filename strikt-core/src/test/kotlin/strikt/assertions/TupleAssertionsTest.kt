package strikt.assertions

import failgood.Test
import failgood.describe
import failgood.testsAbout
import strikt.api.expectThat

@Test
class TupleAssertionsTest {
  val context =
    testsAbout("tuples") {
      describe("on an expectation on a pair") {
        val expectation = expectThat("a" to 1)
        test("first maps to component1") {
          expectation.first.isEqualTo("a")
        }
        test("second maps to component2") {
          expectation.second.isEqualTo(1)
        }
      }
      describe("on an expectation on a triple") {
        val pair = Triple("a", "b", 1)
        val expectation = expectThat(pair)
        test("first maps to component1") {
          expectation.first.isEqualTo("a")
        }
        test("second maps to component2") {
          expectation.second.isEqualTo("b")
        }
        test("third maps to component2") {
          expectation.third.isEqualTo(1)
        }
      }
    }
}
