import java.net.URL

plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly("io.arrow-kt:arrow-core:+")
  testImplementation("io.arrow-kt:arrow-core:+")

  testImplementation("dev.minutest:minutest:+")
}

tasks.dokka {
  configuration {
    "https://arrow-kt.io/docs/apidocs/arrow-core-data/".also {
      externalDocumentationLink {
        url = URL(it)
        packageListUrl = URL(it + "package-list")
      }
    }
  }
}
