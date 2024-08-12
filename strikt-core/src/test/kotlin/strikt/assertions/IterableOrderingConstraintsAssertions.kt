package strikt.assertions

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat

/** Tests for [containsWithOrderingConstraints] */
class IterableOrderingConstraintsAssertions {

  @Test
  fun simpleAfter() {
    expectThat(listOf("a", "b"))
      .containsWithOrderingConstraints {
        expect("a")
        expect("b").after("a")
      }
  }

  @Test
  fun simpleAfterFailure() {
    assertThrows<AssertionError> {
      expectThat(listOf("b", "a"))
        .containsWithOrderingConstraints {
          expect("a")
          expect("b").after("a")
        }
    }
  }

  @Test
  fun simpleAfterNotInOrder() {
    expectThat(listOf("a", "b"))
      .containsWithOrderingConstraints {
        expect("b").after("a")
        expect("a")
      }
  }

  @Test
  fun simpleAfterNotInOrderFailure() {
    assertThrows<AssertionError> {
      expectThat(listOf("b", "a"))
        .containsWithOrderingConstraints {
          expect("b").after("a")
          expect("a")
        }
    }
  }

  @Test
  fun simpleBefore() {
    expectThat(listOf("a", "b"))
      .containsWithOrderingConstraints {
        expect("a").before("b")
        expect("b")
      }
  }

  @Test
  fun simpleBeforeFailure() {
    assertThrows<AssertionError> {
      expectThat(listOf("b", "a"))
        .containsWithOrderingConstraints {
          expect("a").before("b")
          expect("b")
        }
    }
  }

  @Test
  fun simpleBeforeNotInOrder() {
    expectThat(listOf("a", "b"))
      .containsWithOrderingConstraints {
        expect("b")
        expect("a").before("b")
      }
  }

  @Test
  fun simpleBeforeNotInOrderFailure() {
    assertThrows<AssertionError> {
      expectThat(listOf("b", "a"))
        .containsWithOrderingConstraints {
          expect("b")
          expect("a").before("b")
        }
    }
  }

  @Test
  fun beforeAndAfter() {
    expectThat(listOf("a", "b"))
      .containsWithOrderingConstraints {
        expect("a").before("b")
        expect("b").after("a")
      }
  }

  @Test
  fun immediatelyBeforeAndImmediatelyAfter() {
    expectThat(listOf("a", "b", "c", "d"))
      .containsWithOrderingConstraints {
        expect("a").immediatelyBefore("b")
        expect("b")
          .immediatelyAfter("a")
          .immediatelyBefore("c")
        expect("c")
          .immediatelyAfter("b")
          .immediatelyBefore("d")
        expect("d").immediatelyAfter("c")
      }
  }

  @Test
  fun immediatelyBeforeFailure() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "c"))
        .containsWithOrderingConstraints {
          expect("a").immediatelyBefore("c")
        }
    }
  }

  @Test
  fun immediatelyAfterFailure() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "c"))
        .containsWithOrderingConstraints {
          expect("c").immediatelyAfter("a")
        }
    }
  }

  @Test
  fun afterMissingElementFails() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b_wrong", "c"))
        .containsWithOrderingConstraints {
          expect("a")
          expect("c").after("b")
        }
    }
  }

  @Test
  fun firstImmediatelyAfterPreviousAndLast() {
    expectThat(listOf("a", "b", "c", "d"))
      .containsWithOrderingConstraints {
        expect("a").first()
        expect("b").immediatelyAfterPrevious()
        expect("c").immediatelyAfterPrevious()
        expect("d").last()
      }
  }

  @Test
  fun beforeAndAfterInDeclaredOrder() {
    expectThat(listOf("a", "b", "c", "d"))
      .containsWithOrderingConstraints {
        expect("a").before("d")
        expect("b").after("a").before("d")
        expect("c").after("a").after("b").before("d")
        expect("d").after("a")
      }
  }

  @Test
  fun beforeAndAfterInDeclaredOrderFails() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "d", "c"))
        .containsWithOrderingConstraints {
          expect("a").before("d")
          expect("b").after("a").before("d")
          expect("c").after("a").after("b").before("d")
          expect("d").after("a")
        }
    }
  }

  @Test
  fun beforeAndAfterNotInDeclaredOrder() {
    expectThat(listOf("a", "b", "c", "d"))
      .containsWithOrderingConstraints {
        expect("d").after("a")
        expect("a").before("d")
        expect("c").after("a").after("b").before("d")
        expect("b").after("a").before("d")
      }
  }

  @Test
  fun beforeAndAfterNotInDeclaredOrderFails() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "d", "c"))
        .containsWithOrderingConstraints {
          expect("d").after("a")
          expect("a").before("d")
          expect("c").after("a").after("b").before("d")
          expect("b").after("a").before("d")
        }
    }
  }

  @Test
  fun failsWithMissingElements() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "c"))
        .containsWithOrderingConstraints {
          expect("a").first()
          expect("b").immediatelyAfterPrevious()
          expect("c").immediatelyAfterPrevious()
          expect("d").last()
        }
    }
  }

  @Test
  fun passesWithExtraElement() {
    expectThat(listOf("a", "b", "c", "d"))
      .containsWithOrderingConstraints {
        expect("a").first()
        expect("b").immediatelyAfterPrevious()
        expect("c").immediatelyAfterPrevious()
      }
  }

  @Test
  fun failsWithExtraElementAndExpectNoOtherElements() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "c", "d"))
        .containsWithOrderingConstraints {
          expect("a")
          expect("b")
          expect("c")
          expectNoOtherElements()
        }
    }
  }

  @Test
  fun failsWithExtraElementInFirstSectionAndExpectNoOtherElements() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "c", "d"))
        .containsWithOrderingConstraints {
          expect("a")
          expect("c").last()
          expectNoOtherElements()
          startNewSection()
          expect("d")
        }
    }
  }

  @Test
  fun failsWithExtraElementAndExpectNoFurtherElements() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "c", "d"))
        .containsWithOrderingConstraints {
          expect("a").first()
          expect("b").immediatelyAfterPrevious()
          expect("c").immediatelyAfterPrevious()
          expectNoFurtherElements()
        }
    }
  }

  @Test
  fun failsWithExtraElementAfterExpectedLast() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "c", "d"))
        .containsWithOrderingConstraints {
          expect("a").first()
          expect("b").immediatelyAfterPrevious()
          expect("c").last()
        }
    }
  }

  @Test
  fun passesWithElementBeforeExpectedElement() {
    expectThat(listOf("a", "b"))
      .containsWithOrderingConstraints {
        expect("b")
      }
  }

  @Test
  fun failsWithDuplicateElement() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "b", "c"))
        .containsWithOrderingConstraints {
          expect("a").first()
          expect("b").immediatelyAfterPrevious()
          expect("c").last()
        }
    }
  }

  @Test
  fun failsWithDuplicateAssertedElement() {
    assertThrows<IllegalArgumentException> {
      expectThat(listOf("a", "b", "b", "c"))
        .containsWithOrderingConstraints {
          expect("a").first()
          expect("b").immediatelyAfterPrevious()
          expect("b")
          expect("c").last()
        }
    }
  }

  @Test
  fun failsWithWrongFirstElement() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "c"))
        .containsWithOrderingConstraints {
          expect("a")
          expect("b").first()
          expect("c")
        }
    }
  }

  @Test
  fun failsWithWrongLastElement() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "c"))
        .containsWithOrderingConstraints {
          expect("a")
          expect("b").last()
          expect("c")
        }
    }
  }

  @Test
  fun firstAndLastInSections() {
    expectThat(listOf("a", "b", "c", "d"))
      .containsWithOrderingConstraints {
        expect("a").first()
        expect("b").last()
        startNewSection()
        expect("c").first()
        expect("d").last()
      }
  }

  @Test
  fun wrongFirstInSecondSectionFails() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "d", "c"))
        .containsWithOrderingConstraints {
          expect("a").first()
          expect("b").last()
          startNewSection()
          expect("c").first()
          expect("d")
        }
    }
  }

  @Test
  fun wrongLastInSecondSectionFails() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "d", "c"))
        .containsWithOrderingConstraints {
          expect("a").first()
          expect("b").last()
          startNewSection()
          expect("c")
          expect("d").last()
        }
    }
  }

  @Test
  fun lastInPartiallyDeclaredList() {
    expectThat(listOf("a", "b"))
      .containsWithOrderingConstraints {
        expect("b").last()
      }
  }

  @Test
  fun lastInPartiallyDeclaredSections() {
    expectThat(listOf("a", "b", "c", "d"))
      .containsWithOrderingConstraints {
        expect("b").last()
        startNewSection()
        expect("d").last()
      }
  }

  @Test
  fun wrongLastInPartiallyDeclaredSectionsFails() {
    assertThrows<AssertionError> {
      expectThat(listOf("a", "b", "d", "c"))
        .containsWithOrderingConstraints {
          expect("b").last()
          startNewSection()
          expect("d").last()
        }
    }
  }
}
