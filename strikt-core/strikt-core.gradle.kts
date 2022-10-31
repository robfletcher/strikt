plugins {
  kotlin("jvm")
  id("published")
}

description = "The core API for Strikt."

dependencies {
  api("org.opentest4j:opentest4j:${property("versions.opentest4j")}")

  implementation("com.christophsturm:filepeek:${property("versions.filepeek")}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

  testImplementation("dev.failgood:failgood:${property("versions.failgood")}")
  testImplementation("dev.minutest:minutest:${property("versions.minutest")}")
}
