import org.jetbrains.dokka.gradle.DokkaTask

plugins {
  `java-platform`
  id("published")
}

tasks.withType<DokkaTask> {
  enabled = false
}

tasks.withType<Jar> {
  enabled = false
}

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
    api(project(":strikt-jackson"))
    api(project(":strikt-java-time"))
    api(project(":strikt-protobuf"))
  }
}
