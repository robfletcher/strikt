plugins {
  id("java-library")
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly("com.fasterxml.jackson.core:jackson-databind:2.9.8")

  testImplementation("dev.minutest:minutest:1.3.0")
  testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
}
