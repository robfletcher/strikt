rootProject.name = "strikt"

include(
  "strikt-bom",
  "strikt-core",
  "site",
  "strikt-arrow",
  "strikt-jackson",
  "strikt-jvm",
  "strikt-mockk",
  "strikt-protobuf",
  "strikt-spring"
)

rootProject.children.forEach {
  it.buildFileName = "${it.name}.gradle.kts"
}

pluginManagement {
  plugins {
    val versions = mapOf<String, String>()
      .withDefault { extra["versions.$it"].toString() }

    kotlin("jvm") version versions.getValue("kotlin")
    id("org.jetbrains.kotlin.plugin.spring") version versions.getValue("kotlin")
  }
}
