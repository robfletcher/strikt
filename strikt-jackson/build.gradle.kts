import org.jetbrains.dokka.DokkaConfiguration.ExternalDocumentationLink
import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

plugins {
  id("java-library")
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly("com.fasterxml.jackson.core:jackson-databind:2.9.8")

  testImplementation("dev.minutest:minutest:1.3.0")
  testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
}

val dokka by tasks.getting(DokkaTask::class) {
  externalDocumentationLink(delegateClosureOf<ExternalDocumentationLink.Builder> {
    url = URL("https://fasterxml.github.io/jackson-databind/javadoc/2.9/")
  })
}
