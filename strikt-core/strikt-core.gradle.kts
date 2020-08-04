plugins {
  `java-library`
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api("org.opentest4j:opentest4j:1.2.0")
  api("com.christophsturm:filepeek:0.1.2")

  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

  testImplementation("dev.minutest:minutest:1.11.0")
}
