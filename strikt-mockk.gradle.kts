plugins {
  `java-library`
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly("io.mockk:mockk:1.9.3")

  testImplementation("dev.minutest:minutest:1.11.0")
}
