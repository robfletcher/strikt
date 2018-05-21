package strikt.assertions

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import strikt.api.expect
import strikt.fails
import java.time.LocalDate

internal object ComparableAssertions : Spek({
  describe("assertions on ${Comparable::class.simpleName}") {
    describe("isGreaterThan assertion") {
      it("passes if the subject is greater than the expected value") {
        expect(1).isGreaterThan(0)
      }
      it("fails if the subject is equal to the expected value") {
        fails {
          expect(1).isGreaterThan(1)
        }
      }
      it("fails if the subject is less than the expected value") {
        fails {
          expect(LocalDate.of(2018, 5, 1)).isGreaterThan(LocalDate.of(2018, 5, 2))
        }
      }
    }

    describe("isLessThan assertion") {
      it("passes if the subject is less than the expected value") {
        expect(0).isLessThan(1)
      }
      it("fails if the subject is equal to the expected value") {
        fails {
          expect(1).isLessThan(1)
        }
      }
      it("fails if the subject is greater than the expected value") {
        fails {
          expect(LocalDate.of(2018, 5, 2)).isLessThan(LocalDate.of(2018, 5, 1))
        }
      }
    }
    describe("isGreaterThanOrEqualTo assertion") {
      it("passes if the subject is greater than the expected value") {
        expect(1).isGreaterThanOrEqualTo(0)
      }
      it("passes if the subject is equal to the expected value") {
        expect(1).isGreaterThanOrEqualTo(1)
      }
      it("fails if the subject is less than the expected value") {
        fails {
          expect(LocalDate.of(2018, 5, 1)).isGreaterThanOrEqualTo(LocalDate.of(2018, 5, 2))
        }
      }
    }

    describe("isLessThanOrEqualTo assertion") {
      it("passes if the subject is less than the expected value") {
        expect(0).isLessThanOrEqualTo(1)
      }
      it("passes if the subject is equal to the expected value") {
        expect(1).isLessThanOrEqualTo(1)
      }
      it("fails if the subject is greater than the expected value") {
        fails {
          expect(LocalDate.of(2018, 5, 2)).isLessThanOrEqualTo(LocalDate.of(2018, 5, 1))
        }
      }
    }
  }
})