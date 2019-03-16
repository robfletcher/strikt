rootProject.name = "strikt"

include(
  "strikt-core",
  "strikt-jackson",
  "strikt-java-time",
  "strikt-protobuf",
  "strikt-spring",
  "site"
)

enableFeaturePreview("STABLE_PUBLISHING")
