rootProject.name = "strikt"

include(
  "strikt-bom",
  "strikt-core",
  "site",
  "strikt-arrow",
  "strikt-gradle",
  "strikt-jackson",
  "strikt-java-time",
  "strikt-mockk",
  "strikt-protobuf",
  "strikt-spring"
)

rootProject.children.forEach {
  it.buildFileName = "${it.name}.gradle.kts"
}
