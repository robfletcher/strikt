plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

description = "Extensions for assertions and traversals on types from the Mockk mocking and verification library."

dependencies {
  api(project(":strikt-core"))

  compileOnly("io.mockk:mockk:+")

  testImplementation("dev.minutest:minutest:+")
  testImplementation("io.mockk:mockk:+")
}
