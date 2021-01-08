import java.net.URL

plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly(platform("com.fasterxml.jackson:jackson-bom:+"))
  compileOnly("com.fasterxml.jackson.core:jackson-databind")

  testImplementation(platform("com.fasterxml.jackson:jackson-bom:+"))
  testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:+")
  testImplementation("dev.minutest:minutest:+")
}

tasks.dokka {
  configuration {
    "https://fasterxml.github.io/jackson-databind/javadoc/2.12/".also {
      externalDocumentationLink {
        url = URL(it)
        packageListUrl = URL(it + "package-list")
      }
    }
  }
}
