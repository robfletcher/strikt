plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.published)
}

description = "Extensions for assertions and traversals on types from the Mockk mocking and verification library."

dependencies {
  api(project(":strikt-core"))

  compileOnly(libs.mockk)

  testImplementation(libs.minutest)
  testImplementation(libs.mockk)
}
