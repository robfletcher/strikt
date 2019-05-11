package strikt.gradle.testkit

import dev.minutest.junit.testFactoryFor
import dev.minutest.rootContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.TestFactory
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

internal object BuildResultAssertions {

  private class Fixture {
    val mockBuildResult: BuildResult = mockk()
    val assertion: Assertion.Builder<BuildResult> = expectThat(mockBuildResult)
    val mockTask: BuildTask = mockk()
  }

  @TestFactory
  fun task() = testFactoryFor(rootContext<Fixture> {
    fixture {
      Fixture().apply {
        every { mockBuildResult.task(eq(":existingPath")) } returns mockTask
      }
    }

    test("task(path) maps this assertion to an assertion on that task") {
      assertion.task(":existingPath")
        .isNotNull()
        .isEqualTo(mockTask)
      verify { mockBuildResult.task(":existingPath") }
    }
  })

  @TestFactory
  fun taskPaths() = testFactoryFor(rootContext<Fixture> {
    fixture {
      Fixture().apply {
        every { mockBuildResult.taskPaths(TaskOutcome.UP_TO_DATE) } returns listOf(":upToDatePath")
      }
    }

    test("taskPaths(outcome) maps this assertion to an assertion on those task paths") {
      assertion.taskPaths(TaskOutcome.UP_TO_DATE)
        .containsExactly(":upToDatePath")
      verify { mockBuildResult.taskPaths(TaskOutcome.UP_TO_DATE) }
    }
  })

  @TestFactory
  fun tasks() = testFactoryFor(rootContext<Fixture> {
    fixture {
      Fixture().apply {
        every { mockBuildResult.tasks } returns listOf(mockTask)
        every { mockBuildResult.tasks(TaskOutcome.UP_TO_DATE) } returns listOf(mockTask)
      }
    }

    test("tasks maps this assertion to an assertion on the tasks") {
      assertion.tasks
        .containsExactly(mockTask)
      verify { mockBuildResult.tasks }
    }

    test("tasks(outcome) maps this assertion to an assertion on those tasks") {
      assertion.tasks(TaskOutcome.UP_TO_DATE)
        .containsExactly(mockTask)
      verify { mockBuildResult.tasks(TaskOutcome.UP_TO_DATE) }
    }
  })

  @TestFactory
  fun output() = testFactoryFor(rootContext<Fixture> {
    fixture {
      Fixture().apply {
        every { mockBuildResult.output } returns "build output"
      }
    }
    test("output maps this assertion to an assertion on the output") {
      assertion.output
        .isEqualTo("build output")
      verify { mockBuildResult.output }
    }
  })
}
