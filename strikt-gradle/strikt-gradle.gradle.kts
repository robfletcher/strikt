import org.gradle.util.GradleVersion
import java.net.URL

plugins {
  kotlin("jvm")
  id("published")
}

description = "Extensions for assertions and traversals on Gradle's test kit."

dependencies {
  api(project(":strikt-core"))

  compileOnly(gradleTestKit())

  testImplementation(gradleTestKit())
  testImplementation("io.mockk:mockk:${property("versions.mockk")}")
  testImplementation("dev.minutest:minutest:${property("versions.minutest")}")
}

tasks.dokka {
  configuration {
    "https://docs.gradle.org/${GradleVersion.current().version}/javadoc/".also {
      externalDocumentationLink {
        url = URL(it)
        packageListUrl = URL(it + "package-list")
      }
    }
  }
}
