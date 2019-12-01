package strikt.gradle.testkit

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import strikt.api.Assertion

/**
 * Maps this assertion to the task at the provided [taskPath].
 * @see BuildResult.task
 */
infix fun <T : BuildResult> Assertion.Builder<T>.task(taskPath: String): Assertion.Builder<BuildTask?> =
  get("task path") { task(taskPath) }

/**
 * Maps this assertion to the task paths of the build with the provided [outcome].
 * @see BuildResult.taskPaths
 */
infix fun <T : BuildResult> Assertion.Builder<T>.taskPaths(outcome: TaskOutcome): Assertion.Builder<List<String>> =
  get("task paths with outcome $outcome") { taskPaths(outcome) }

/**
 * Maps this assertion to an assertion on all tasks that were part of the build.
 * @see BuildResult.getTasks
 */
val <T : BuildResult> Assertion.Builder<T>.tasks: Assertion.Builder<List<BuildTask>>
  get() = get("all tasks", BuildResult::getTasks)

/**
 * Maps this assertion to the tasks of the build with the provided [outcome].
 * @see BuildResult
 * @see TaskOutcome
 */
infix fun <T : BuildResult> Assertion.Builder<T>.tasks(outcome: TaskOutcome): Assertion.Builder<List<BuildTask>> =
  get("tasks with outcome $outcome") { tasks(outcome) }

/**
 * Maps this assertion to an assertion on the output.
 * @see BuildResult
 */
val <T : BuildResult> Assertion.Builder<T>.output: Assertion.Builder<String>
  get() = get("output", BuildResult::getOutput)
