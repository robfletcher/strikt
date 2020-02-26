import java.net.URL

plugins {
  `java-library`
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly("io.arrow-kt:arrow-core:0.10.4")
  testImplementation("io.arrow-kt:arrow-core:0.10.4")

  testImplementation("dev.minutest:minutest:1.11.0")
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
