import java.net.URL

plugins {
  `java-library`
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly("com.fasterxml.jackson.core:jackson-databind:2.10.3")

  testImplementation("dev.minutest:minutest:1.11.0")
  testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
}

tasks.dokka {
  configuration {
    "https://fasterxml.github.io/jackson-databind/javadoc/2.10/".also {
      externalDocumentationLink {
        url = URL(it)
        packageListUrl = URL(it + "package-list")
      }
    }
  }
}
