plugins {
  id("java-library")
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

repositories {
  maven(url = "https://dl.bintray.com/dmcg/oneeyedmen-mvn")
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:+")
  implementation("org.opentest4j:opentest4j:+")

  testImplementation("com.oneeyedmen:minutest:+")
}
