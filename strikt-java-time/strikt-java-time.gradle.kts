plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  testImplementation("dev.minutest:minutest:+")
}
