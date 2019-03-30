rootProject.name = "strikt"

include(
  "strikt-bom",
  "strikt-core",
  "strikt-jackson",
  "strikt-java-time",
  "strikt-protobuf",
  "strikt-spring",
  "site"
)

enableFeaturePreview("STABLE_PUBLISHING")

rootProject.children.forEach {
  it.buildFileName = "${it.name}.gradle.kts"
}
