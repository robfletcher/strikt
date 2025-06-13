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
  includeBuild("buildLogic")
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    @Suppress("DEPRECATION")
    jcenter {
      content { includeGroup("io.github.javaeden.orchid") }
    }
  }
}
