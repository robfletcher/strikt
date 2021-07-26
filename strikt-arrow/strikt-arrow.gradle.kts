import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

description = "Extensions for assertions and traversals on types from the Arrow functional programming library."

dependencies {
  api(project(":strikt-core"))

  compileOnly("io.arrow-kt:arrow-core:${property("versions.arrow")}")
  testImplementation("io.arrow-kt:arrow-core:${property("versions.arrow")}")

  testImplementation("dev.minutest:minutest:${property("versions.minutest")}")
}

tasks.withType<DokkaTaskPartial>().configureEach {
  dokkaSourceSets {
    configureEach {
      "https://arrow-kt.io/docs/apidocs/arrow-core/".also {
        externalDocumentationLink {
          url.set(URL(it))
          packageListUrl.set(URL("${it}package-list"))
        }
      }
    }
  }
}
