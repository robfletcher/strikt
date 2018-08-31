import org.gradle.api.JavaVersion.VERSION_1_6

plugins {
  id("java-library")
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.opentest4j:opentest4j:+")
}
