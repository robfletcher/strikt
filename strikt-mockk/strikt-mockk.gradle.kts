plugins {
  kotlin("jvm")
  id("published")
}

description = "Extensions for assertions and traversals on types from the Mockk mocking and verification library."

dependencies {
  api(project(":strikt-core"))

  compileOnly("io.mockk:mockk:${property("versions.mockk")}")

  testImplementation("dev.minutest:minutest:${property("versions.minutest")}")
  testImplementation("io.mockk:mockk:${property("versions.mockk")}")
}
