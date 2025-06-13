import org.gradle.util.GradleVersion
import java.net.URL

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.published)
}

description = "Extensions for assertions and traversals on Gradle's test kit."

dependencies {
  api(project(":strikt-core"))

  compileOnly(gradleTestKit())

  testImplementation(gradleTestKit())
  testImplementation(libs.mockk)
  testImplementation(libs.minutest)
}

tasks.dokka {
  configuration {
    "https://docs.gradle.org/${GradleVersion.current().version}/javadoc/".also {
      externalDocumentationLink {
        url = URI(it).toURL()
        packageListUrl = URI(it + "package-list").toURL()
      }
    }
  }
}
