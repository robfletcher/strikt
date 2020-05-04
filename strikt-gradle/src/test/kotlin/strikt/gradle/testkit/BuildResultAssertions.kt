package strikt.gradle.testkit

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome.FAILED
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.contains
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.message

internal object BuildResultAssertions : JUnit5Minutests {

  private class Fixture {
    val mockBuildResult: BuildResult = mockk()
    val assertion: Assertion.Builder<BuildResult> = expectThat(mockBuildResult)
    val mockTask: BuildTask = mockk()
  }

  fun tests() = rootContext<Fixture> {
    context("task") {
      fixture {
        Fixture().apply {
          every { mockBuildResult.task(eq(":existingPath")) } returns mockTask
          every { mockTask.outcome } returns FAILED
        }
      }

      test("task(path) maps this assertion to an assertion on that task") {
        assertion.task(":existingPath")
          .isNotNull()
          .isEqualTo(mockTask)
        verify { mockBuildResult.task(":existingPath") }
      }

      test("task path is included in any diagnostic information") {
        expectThrows<AssertionFailedError> {
          assertion.task(":existingPath").isNotNull().isSuccess()
        }
          .message
          .isNotNull()
          .contains("▼ task path :existingPath:")
      }
    }

    context("taskPaths") {
      fixture {
        Fixture().apply {
          every {
            mockBuildResult.taskPaths(UP_TO_DATE)
          } returns listOf(":upToDatePath")
        }
      }

      test("taskPaths(outcome) maps this assertion to an assertion on those task paths") {
        assertion.taskPaths(UP_TO_DATE).containsExactly(":upToDatePath")
        verify { mockBuildResult.taskPaths(UP_TO_DATE) }
      }
    }

    context("tasks") {
      fixture {
        Fixture().apply {
          every { mockBuildResult.tasks } returns listOf(mockTask)
          every { mockBuildResult.tasks(UP_TO_DATE) } returns listOf(mockTask)
        }
      }

      test("tasks maps this assertion to an assertion on the tasks") {
        assertion.tasks.containsExactly(mockTask)
        verify { mockBuildResult.tasks }
      }

      test("tasks(outcome) maps this assertion to an assertion on those tasks") {
        assertion.tasks(UP_TO_DATE).containsExactly(mockTask)
        verify { mockBuildResult.tasks(UP_TO_DATE) }
      }
    }

    context("output") {
      fixture {
        Fixture().apply {
          every { mockBuildResult.output } returns "build output"
        }
      }
      test("output maps this assertion to an assertion on the output") {
        assertion.output.isEqualTo("build output")
        verify { mockBuildResult.output }
      }
    }
  }
}
