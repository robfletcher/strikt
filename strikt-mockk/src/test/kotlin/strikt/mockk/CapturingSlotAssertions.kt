package strikt.mockk

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.function.Consumer

class CapturingSlotAssertions : JUnit5Minutests {

  private class Fixture {
    val slot = slot<String>()
    val mockFunction: Consumer<String> = mockk() {
      every { accept(capture(slot)) } just Runs
    }
  }

  fun tests() = rootContext<Fixture> {
    fixture {
      Fixture()
    }

    context("isCaptured assertion") {
      test("fails if nothing has been captured") {
        assertThrows<AssertionFailedError> {
          expectThat(slot).isCaptured()
        }
      }

      test("passes if a value has been captured") {
        mockFunction.accept("fnord")
        expectThat(slot).isCaptured()
      }
    }

    context("captured mapping") {
      test("maps the assertion to the captured value") {
        mockFunction.accept("fnord")
        expectThat(slot).captured.isEqualTo("fnord")
      }

      test("does something unexpected if nothing has been captured") {
        assertThrows<IllegalStateException> {
          expectThat(slot).captured
        }
      }
    }
  }
}
