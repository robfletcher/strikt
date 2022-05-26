plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

description = "The core API for Strikt."

dependencies {
  api("org.opentest4j:opentest4j:${property("versions.opentest4j")}")

  implementation("com.christophsturm:filepeek:${property("versions.filepeek")}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

  testImplementation("dev.failgood:failgood:0.7.1")
  testImplementation("dev.minutest:minutest:${property("versions.minutest")}")
}
