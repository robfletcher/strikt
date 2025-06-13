plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.published)
}

description = "The core API for Strikt."

dependencies {
  api(libs.opentest4j)

  implementation(libs.filepeek)
  implementation(libs.kotlinx.coroutines.core)

  testImplementation(libs.failgood)
  testImplementation(libs.minutest)
}
