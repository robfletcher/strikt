package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

@DisplayName("assertions on ClosedRange")
internal object ClosedRangeAssertions {

  @TestFactory
  fun contains() = assertionTests<ClosedRange<Int>> {
    context("an empty IntRange") {
      fixture { expectThat(IntRange.EMPTY) }

      test("the assertion fails to contain any value") {
        assertThrows<AssertionError> {
          contains(0)
        }
      }
    }

    context("an IntRange from 1 to 4") {
      fixture { expectThat(1..4) }

      test("assertion fails to contain 0") {
        assertThrows<AssertionError> {
          contains(0)
        }
      }

      (1..4).forEach { value ->
        test("assertion passes for containing $value") {
          contains(value)
        }
      }
    }
  }

  @TestFactory
  fun isEmpty() = assertionTests<IntRange> {
    context("an empty IntRange") {
      fixture { expectThat(IntRange.EMPTY) }

      test("the assertion succeeds") {
        isEmpty()
      }
    }
    context("a nonempty IntRange") {
      fixture { expectThat(2..2) }

      test("the assertion fails") {
        assertThrows<AssertionError> {
          isEmpty()
        }
      }
    }
  }
}
