plugins {
  `java-library`
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.2")
  implementation("org.opentest4j:opentest4j:1.2.0")

  testImplementation("dev.minutest:minutest:1.7.0")
}
