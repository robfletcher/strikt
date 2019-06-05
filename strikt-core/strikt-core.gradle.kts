plugins {
  `java-library`
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

repositories {
  maven(url = "https://dl.bintray.com/dmcg/oneeyedmen-mvn")
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
  implementation("org.opentest4j:opentest4j:1.1.1")

  testImplementation("dev.minutest:minutest:1.7.0")
}
