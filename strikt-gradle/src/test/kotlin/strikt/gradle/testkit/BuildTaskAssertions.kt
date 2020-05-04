package strikt.gradle.testkit

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.TaskOutcome.FAILED
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.assertThrows
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal object BuildTaskAssertions : JUnit5Minutests {

  private class Fixture {
    val mockBuildTask: BuildTask = mockk()
    val assertion: Assertion.Builder<BuildTask> = expectThat(mockBuildTask)
  }

  fun tests() = rootContext<Fixture> {
    context("hasOutcome") {
      fixture {
        Fixture().apply {
          every { mockBuildTask.outcome } returns SUCCESS
        }
      }

      test("when provided outcome is equal then the assertion passes") {
        assertion.hasOutcome(SUCCESS)
      }

      test("when provided outcome is not equal then the assertion fails") {
        assertThrows<AssertionError> {
          assertion.hasOutcome(FAILED)
        }
      }
    }

    context("outcome") {
      fixture {
        Fixture().apply {
          every { mockBuildTask.outcome } returns SUCCESS
        }
      }

      test("outcome maps to an assertion on the task outcome") {
        assertion.outcome.isEqualTo(SUCCESS)
        verify { mockBuildTask.outcome }
      }
    }

    context("isSuccess") {
      fixture { Fixture() }

      context("when outcome is equal") {
        modifyFixture {
          every { mockBuildTask.outcome }.returns(SUCCESS)
        }

        test("then assertion succeeds") {
          assertion.isSuccess()
        }
      }

      context("when outcome is not equal") {
        modifyFixture {
          every { mockBuildTask.outcome }.returns(FAILED)
        }

        test("then assertions fails") {
          assertThrows<AssertionError> {
            assertion.isSuccess()
          }
        }
      }
    }

    context("isFailed") {
      fixture { Fixture() }

      context("when outcome is equal") {
        modifyFixture {
          every { mockBuildTask.outcome }.returns(FAILED)
        }

        test("then assertion succeeds") {
          assertion.isFailed()
        }
      }

      context("when outcome is not equal") {
        modifyFixture {
          every { mockBuildTask.outcome }.returns(SUCCESS)
        }

        test("then assertions fails") {
          assertThrows<AssertionError> {
            assertion.isFailed()
          }
        }
      }
    }

    context("isUpToDate") {
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
          every { mockBuildTask.outcome }.returns(FAILED)
        }

        test("then assertions fails") {
          assertThrows<AssertionError> {
            assertion.isUpToDate()
          }
        }
      }
    }

    context("isSkipped") {
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
          every { mockBuildTask.outcome }.returns(FAILED)
        }

        test("then assertions fails") {
          assertThrows<AssertionError> {
            assertion.isSkipped()
          }
        }
      }
    }

    context("isFromCache") {
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
          every { mockBuildTask.outcome }.returns(FAILED)
        }

        test("then assertions fails") {
          assertThrows<AssertionError> {
            assertion.isFromCache()
          }
        }
      }
    }

    context("isNoSource") {
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
          every { mockBuildTask.outcome }.returns(FAILED)
        }

        test("then assertions fails") {
          assertThrows<AssertionError> {
            assertion.isNoSource()
          }
        }
      }
    }

    context("path") {
      fixture {
        Fixture().apply {
          every { mockBuildTask.path } returns ":taskPath"
        }
      }

      test("path maps to an assertion on the task path") {
        assertion.path.isEqualTo(":taskPath")
        verify { mockBuildTask.path }
      }
    }
  }
}
