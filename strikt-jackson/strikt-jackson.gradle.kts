import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URI

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.published)
}

description = "Extensions for assertions and traversals on types Jackson's JsonNode and sub-types."

dependencies {
  api(project(":strikt-core"))

  compileOnly(platform(libs.jackson.bom))
  compileOnly(libs.jackson.databind)

  testImplementation(platform(libs.jackson.bom))
  testImplementation(libs.jackson.module.kotlin)
  testImplementation(libs.minutest)
}

tasks.withType<DokkaTaskPartial>().configureEach {
  dokkaSourceSets {
    configureEach {
    "https://fasterxml.github.io/jackson-databind/javadoc/2.12/".also {
      externalDocumentationLink {
        url.set(URI(it).toURL())
        packageListUrl.set(URI("${it}package-list").toURL())
      }
    }
    }
  }
}
