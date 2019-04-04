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
    api(project(":strikt-core"))
    api(project(":strikt-gradle"))
    api(project(":strikt-jackson"))
    api(project(":strikt-java-time"))
    api(project(":strikt-protobuf"))
  }
}
