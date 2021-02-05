plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

description = "Extensions for assertions and traversals on types from the java.time package."

dependencies {
  api(project(":strikt-core"))

  testImplementation("dev.minutest:minutest:+")
}
