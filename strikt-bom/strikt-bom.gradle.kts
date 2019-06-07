plugins {
  `java-platform`
  id("published")
}

description =
  "Bill of materials to make sure a consistent set of versions is used for Strikt."

publishing {
  publications {
    getByName<MavenPublication>("nebula") {
      from(components["javaPlatform"])
    }
  }
}

dependencies {
  constraints {
    rootProject
      .subprojects
      .filter { it.includeInBom }
      .forEach {
        api(project(it.path))
      }
  }
}

@Suppress("KDocMissingDocumentation")
val Project.includeInBom: Boolean
  get() = with(plugins) {
    hasPlugin("published") && !hasPlugin("org.gradle.java-platform")
  }
