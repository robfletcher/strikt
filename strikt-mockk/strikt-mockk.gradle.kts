plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly("io.mockk:mockk:+")

  testImplementation("dev.minutest:minutest:+")
  testImplementation("io.mockk:mockk:+")
}
