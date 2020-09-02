plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly("io.mockk:mockk:1.10.0")

  testImplementation("dev.minutest:minutest:1.11.0")
  testImplementation("io.mockk:mockk:1.10.0")
}
