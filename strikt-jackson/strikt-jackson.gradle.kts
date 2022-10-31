import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

plugins {
  kotlin("jvm")
  id("published")
}

description = "Extensions for assertions and traversals on types Jackson's JsonNode and sub-types."

dependencies {
  api(project(":strikt-core"))

  compileOnly(platform("com.fasterxml.jackson:jackson-bom:${property("versions.jackson")}"))
  compileOnly("com.fasterxml.jackson.core:jackson-databind")

  testImplementation(platform("com.fasterxml.jackson:jackson-bom:${property("versions.jackson")}"))
  testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  testImplementation("dev.minutest:minutest:${property("versions.minutest")}")
}

tasks.withType<DokkaTaskPartial>().configureEach {
  dokkaSourceSets {
    configureEach {
    "https://fasterxml.github.io/jackson-databind/javadoc/2.12/".also {
      externalDocumentationLink {
        url.set(URL(it))
        packageListUrl.set(URL("${it}package-list"))
      }
    }
    }
  }
}
