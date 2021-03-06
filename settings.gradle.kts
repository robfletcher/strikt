rootProject.name = "strikt"

enableFeaturePreview("ONE_LOCKFILE_PER_PROJECT")

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
