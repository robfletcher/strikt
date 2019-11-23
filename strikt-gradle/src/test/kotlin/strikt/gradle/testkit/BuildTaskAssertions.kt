package strikt.gradle.testkit

import dev.minutest.junit.testFactoryFor
import dev.minutest.rootContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal object BuildTaskAssertions {

  private class Fixture {
    val mockBuildTask: BuildTask = mockk()
    val assertion: Assertion.Builder<BuildTask> = expectThat(mockBuildTask)
  }

  @TestFactory
  fun hasOutcome() = testFactoryFor(rootContext<Fixture> {
    fixture {
      Fixture().apply {
        every { mockBuildTask.outcome } returns TaskOutcome.SUCCESS
      }
    }
    test("when provided outcome is equal then the assertion passes") {
      assertion.hasOutcome(TaskOutcome.SUCCESS)
    }

    test("when provided outcome is not equal then the assertion fails") {
      assertThrows<AssertionError> {
        assertion.hasOutcome(TaskOutcome.FAILED)
      }
    }
  })

  @TestFactory
  fun outcome() = testFactoryFor(rootContext<Fixture> {
    fixture {
      Fixture().apply {
        every { mockBuildTask.outcome } returns TaskOutcome.SUCCESS
      }
    }
    test("outcome maps to an assertion on the task outcome") {
      assertion.outcome.isEqualTo(TaskOutcome.SUCCESS)
      verify { mockBuildTask.outcome }
    }
  })

  @TestFactory
  fun isSuccess() = testFactoryFor(rootContext<Fixture> {
    fixture { Fixture() }
    context("when outcome is equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.SUCCESS)
      }
      test("then assertion succeeds") {
        assertion.isSuccess()
      }
    }

    context("when outcome is not equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.FAILED)
      }
      test("then assertions fails") {
        assertThrows<AssertionError> {
          assertion.isSuccess()
        }
      }
    }
  })

  @TestFactory
  fun isFailed() = testFactoryFor(rootContext<Fixture> {
    fixture { Fixture() }
    context("when outcome is equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.FAILED)
      }
      test("then assertion succeeds") {
        assertion.isFailed()
      }
    }

    context("when outcome is not equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.SUCCESS)
      }
      test("then assertions fails") {
        assertThrows<AssertionError> {
          assertion.isFailed()
        }
      }
    }
  })

  @TestFactory
  fun isUpToDate() = testFactoryFor(rootContext<Fixture> {
    fixture { Fixture() }
    context("when outcome is equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.UP_TO_DATE)
      }
      test("then assertion succeeds") {
        assertion.isUpToDate()
      }
    }

    context("when outcome is not equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.FAILED)
      }
      test("then assertions fails") {
        assertThrows<AssertionError> {
          assertion.isUpToDate()
        }
      }
    }
  })

  @TestFactory
  fun isSkipped() = testFactoryFor(rootContext<Fixture> {
    fixture { Fixture() }
    context("when outcome is equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.SKIPPED)
      }
      test("then assertion succeeds") {
        assertion.isSkipped()
      }
    }

    context("when outcome is not equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.FAILED)
      }
      test("then assertions fails") {
        assertThrows<AssertionError> {
          assertion.isSkipped()
        }
      }
    }
  })

  @TestFactory
  fun isFromCache() = testFactoryFor(rootContext<Fixture> {
    fixture { Fixture() }
    context("when outcome is equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.FROM_CACHE)
      }
      test("then assertion succeeds") {
        assertion.isFromCache()
      }
    }

    context("when outcome is not equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.FAILED)
      }
      test("then assertions fails") {
        assertThrows<AssertionError> {
          assertion.isFromCache()
        }
      }
    }
  })

  @TestFactory
  fun isNoSource() = testFactoryFor(rootContext<Fixture> {
    fixture { Fixture() }
    context("when outcome is equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.NO_SOURCE)
      }
      test("then assertion succeeds") {
        assertion.isNoSource()
      }
    }

    context("when outcome is not not equal") {
      modifyFixture {
        every { mockBuildTask.outcome }.returns(TaskOutcome.FAILED)
      }
      test("then assertions fails") {
        assertThrows<AssertionError> {
          assertion.isNoSource()
        }
      }
    }
  })

  @TestFactory
  fun path() = testFactoryFor(rootContext<Fixture> {
    fixture {
      Fixture().apply {
        every { mockBuildTask.path } returns ":taskPath"
      }
    }
    test("path maps to an assertion on the task path") {
      assertion.path.isEqualTo(":taskPath")
      verify { mockBuildTask.path }
    }
  })
}
