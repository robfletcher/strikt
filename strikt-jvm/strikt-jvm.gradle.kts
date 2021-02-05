plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

description = "Extensions for assertions and traversals on types from the Java standard library."

dependencies {
  api(project(":strikt-core"))

  testImplementation("dev.minutest:minutest:${property("versions.minutest")}")
}
